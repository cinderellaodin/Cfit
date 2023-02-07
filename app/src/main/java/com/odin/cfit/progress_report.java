package com.odin.cfit;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.odin.cfit.adapter.FitNewsAdapter;
import com.odin.cfit.adapter.WeightTrackerAdapter;
import com.odin.cfit.model.News;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

/*import android.support.v4.app.Fragment;*/


public class progress_report extends Fragment {
    RecyclerView recyclerView;
    private WeightTrackerAdapter mwtrackerAdapter;
    List<WeighTracker> mWeighTracker;

    private View parent_view;
    private View back_drop;
    private boolean rotate = false;
    private View lyt_tape;
    private View lyt_scale;


    private FirebaseAuth firebaseAuth;
    private FirebaseStorage mStorage;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ValueEventListener mDBListener;

    FirebaseUser user;


    private ProgressBar mProgressCircle;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout lyt_no_connection;

    int day, month, year, yearfinal;
    Calendar calendar;
    DatePickerDialog datePickerDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Progress Report");

        /*firebase*/
        //initializing firebase
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getContext(), login.class));

        }
        /*FirebaseUser*/
        user = firebaseAuth.getCurrentUser();
        //db reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        mProgressCircle = (ProgressBar) view.findViewById(R.id.progress_circle);
       // lyt_no_connection = (LinearLayout) view.findViewById(R.id.lyt_no_connection);

        retrieveData();
       // isNetworkConnected();

        /*pull to refres*/
      //  swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
      /*  swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.


                // mPostAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }
        });*/

        //line chart
      LineChartView lineChartView = view.findViewById(R.id.chart);



        /*for fab menu*/
        parent_view = view.findViewById(android.R.id.content);
        back_drop = view.findViewById(R.id.back_drop);
        /*buttons in fab menu*/
        final FloatingActionButton fab_body = (FloatingActionButton) view.findViewById(R.id.fab_body);
        final FloatingActionButton fab_weight = (FloatingActionButton) view.findViewById(R.id.fab_weight);
        final FloatingActionButton fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);

        lyt_tape = view.findViewById(R.id.lyt_tape);
        lyt_scale = view.findViewById(R.id.lyt_scale);
        ViewAnimation.initShowOut(lyt_tape);
        ViewAnimation.initShowOut(lyt_scale);
        back_drop.setVisibility(View.GONE);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMode(view);
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMode(fab_add);
            }
        });


      /*  lyt_no_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                isNetworkConnected();
                mProgressCircle.setVisibility(View.VISIBLE);
                lyt_no_connection.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressCircle.setVisibility(View.GONE);
                        lyt_no_connection.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        });*/

        fab_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ReportDialog();
                Intent intent = new Intent(getActivity(), progressUpdate.class);
                startActivity(intent);


            }
        });

        fab_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReportDialog();

            }
        });

    }

    private void ReportDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.weightdialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button bt_submit = (Button) dialog.findViewById(R.id.updateweight);
        EditText etCdate =(EditText) dialog.findViewById(R.id.weigthdate);
        EditText etCWeight =(EditText) dialog.findViewById(R.id.weigtupdate);

        etCdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                //used for age
                yearfinal = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tdate = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(dayOfMonth);
                        etCdate.setText(tdate);

                      /*  int yeardiff = yearfinal - year;
                        uage = String.valueOf(yeardiff);*/
                        // etdob.setText(uage);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeight(etCdate.getText().toString().trim(), Double.
                        parseDouble(etCWeight.getText().toString().trim()));
                dialog.dismiss();
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
            }


        });



        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void saveWeight(String cDate, double cWeightProgress) {
        WeighTracker weightTracker = new WeighTracker(cDate, cWeightProgress);
        String uploadCId =   databaseReference.push().getKey();
        databaseReference.child(user.getUid()).child("Weight Tracker").child(uploadCId).setValue(weightTracker);

    }


    /*for fab menu animation*/
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_tape);
            ViewAnimation.showIn(lyt_scale);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_tape);
            ViewAnimation.showOut(lyt_scale);
            back_drop.setVisibility(View.GONE);
        }
    }

    private void retrieveData() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);
        mWeighTracker = new ArrayList<>();
        mwtrackerAdapter = new WeightTrackerAdapter(getActivity(), mWeighTracker);

        mDBListener = databaseReference.child(user.getUid()).child("Weight Tracker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mWeighTracker.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    WeighTracker weights = ds.getValue(WeighTracker.class);
                    weights.setKey(ds.getKey());
                    mWeighTracker.add(weights);
                    if (mWeighTracker.size()<=0) {
                        Toast.makeText(getActivity(), "No Posts yet\n be sure to post some fashion trends", Toast.LENGTH_SHORT).show();

                    }else{
                        recyclerView.setAdapter(mwtrackerAdapter);
                       // Toast.makeText(getActivity(), "weight"+ weights, Toast.LENGTH_SHORT).show();

                    }
                }

                mwtrackerAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

  /*  private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                // retrieveData();
                lyt_no_connection.setVisibility(View.INVISIBLE);
                swipeContainer.setRefreshing(false);

            } else {

                mProgressCircle.setVisibility(View.GONE);
                lyt_no_connection.setVisibility(View.VISIBLE);
                swipeContainer.setRefreshing(false);
            }
        }
        return false;

    }*/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress_report, container, false);
    }

}
