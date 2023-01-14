package com.odin.cfit;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

/*import android.support.v4.app.Fragment;*/


public class progress_report extends Fragment {

    private View parent_view;
    private View back_drop;
    private boolean rotate = false;
    private View lyt_gallery;
    private View lyt_camera;
    FloatingActionButton fab_add, fab_camera, fab_gallery;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage mStorage;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ValueEventListener mDBListener;
    private Uri filepath, photocam;
    FirebaseUser user;
    RecyclerView recyclerView;

    private WeightTrackerAdapter mwtrackerAdapter;
    List<WeighTracker> mWeighTracker;

    private ProgressBar mProgressCircle;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout lyt_no_connection;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private int REQUEST_CODE = 1;

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
     //   LineChartView lineChartView = view.findViewById(R.id.chart);



        /*for fab menu*/
        parent_view = view.findViewById(android.R.id.content);
        back_drop = view.findViewById(R.id.back_drop);
        /*buttons in fab menu*/
        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab_camera = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        fab_gallery = (FloatingActionButton) view.findViewById(R.id.fab_gallery);

        lyt_gallery = view.findViewById(R.id.lyt_gallery);
        lyt_camera = view.findViewById(R.id.lyt_camera);
        ViewAnimation.initShowOut(lyt_gallery);
        ViewAnimation.initShowOut(lyt_camera);
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


        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), REQUEST_CODE);

            }
        });

        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "camera", Toast.LENGTH_SHORT).show();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

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

    }

    private void retrieveData() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);
        mWeighTracker = new ArrayList<>();
        mwtrackerAdapter = new WeightTrackerAdapter(getActivity(), mWeighTracker);

        mDBListener = databaseReference.child("Weight Tracker").addValueEventListener(new ValueEventListener() {
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

    /*for fab menu animation*/
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_gallery);
            ViewAnimation.showIn(lyt_camera);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_gallery);
            ViewAnimation.showOut(lyt_camera);
            back_drop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath = data.getData());
                // ((ImageView) getView().findViewById(R.id.cropImageView)).setImageBitmap(bitmap);
                Intent intent = new Intent(getActivity(), progressUpdate.class);
                intent.setData(filepath);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            photocam = data.getData();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            Intent intent = new Intent(getActivity(), progressUpdate.class);
            intent.setData(photocam);
            startActivity(intent);

        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress_report, container, false);
    }

}
