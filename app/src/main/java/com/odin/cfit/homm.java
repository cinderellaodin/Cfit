package com.odin.cfit;
/*home fragment */


import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.odin.cfit.adapter.FitNewsAdapter;
import com.odin.cfit.model.News;

import java.util.ArrayList;
import java.util.List;


public class homm extends Fragment {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private View parent_view;
    private FitNewsAdapter mfitAdapter;
    List<News> mNews;



    private View back_drop;
    private boolean rotate = false;
    private View lyt_tape;
    private View lyt_scale;
    private View lyt_post;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage mStorage;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ValueEventListener mDBListener;
    FirebaseUser user;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       getActivity().setTitle("CleanFit Community");

        /*firebase*/
        //initializing firebase
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            getActivity().finish();
            startActivity(new Intent(getContext(), login.class));

        }
        /*FirebaseUser*/
        user = firebaseAuth.getCurrentUser();
        //db reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        initComponent();
        parent_view = view.findViewById(android.R.id.content);
        back_drop = view.findViewById(R.id.back_drop);
         toolbar = (Toolbar) view.findViewById(R.id.toolbar);



        final FloatingActionButton fab_body = (FloatingActionButton) view.findViewById(R.id.fab_body);
        final FloatingActionButton fab_weight = (FloatingActionButton) view.findViewById(R.id.fab_weight);
        final FloatingActionButton fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
        final FloatingActionButton fab_post = (FloatingActionButton) view.findViewById(R.id.fab_new_post);

        lyt_tape = view.findViewById(R.id.lyt_tape);
        lyt_scale = view.findViewById(R.id.lyt_scale);
        lyt_post = view.findViewById(R.id.lyt_post);
        ViewAnimation.initShowOut(lyt_tape);
        ViewAnimation.initShowOut(lyt_scale);
        ViewAnimation.initShowOut(lyt_post);
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

        fab_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        fab_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        fab_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();


            }
        });

    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_tape);
            ViewAnimation.showIn(lyt_scale);
            ViewAnimation.showIn(lyt_post);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_tape);
            ViewAnimation.showOut(lyt_scale);
            ViewAnimation.showOut(lyt_post);
            back_drop.setVisibility(View.GONE);
        }
    }



    private void initComponent() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);
        mNews = new ArrayList<>();
        mfitAdapter = new FitNewsAdapter(getActivity(), mNews);

        mDBListener = databaseReference.child("fitnews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNews.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    News posts = ds.getValue(News.class);
                    posts.setKey(ds.getKey());
                    mNews.add(posts);
                    if (mNews.size()<=0) {
                        Toast.makeText(getActivity(), "No Posts yet\n be sure to post some fashion trends", Toast.LENGTH_SHORT).show();


                    }else{
                        recyclerView.setAdapter(mfitAdapter);


                    }
                }

                mfitAdapter.notifyDataSetChanged();

                //mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
               // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });





        // on item list clicked
        mfitAdapter.setOnItemClickListener(new FitNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Snackbar.make(parent_view, "Item " + position + " clicked", Snackbar.LENGTH_SHORT).show();

            }
        });

    }

    /*custom dialog*/

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_post);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        final AppCompatButton bt_submit = (AppCompatButton) dialog.findViewById(R.id.bt_submit);
        ((EditText) dialog.findViewById(R.id.et_post)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_submit.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Post Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Post Photo Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Post Link Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_file)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Post File Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Post Setting Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homm, container, false);
    }

}
