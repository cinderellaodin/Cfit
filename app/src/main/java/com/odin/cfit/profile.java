package com.odin.cfit;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.odin.cfit.model.UserInformation;


public class profile extends Fragment implements View.OnClickListener {
    final int REQUEST_CODE = 1;
    final int Request_code = 1;
    FirebaseUser user;
    //Button btnEditmode;
    FloatingActionButton btnEditmode, btneditProfile, btnAbout;
    CircularImageView cImageView;
    TextView tvname, tvemail, tvgender, tvage, tvdob, tvheight, tvweight, tvgweight, tvactivity, tvbmi, tvbmicon, tvdailycal, etdob, valuerb;
    double uheight, uweight, ugweight;
    String ugender, uname, agee, udob, uactivity, ubmi, ubmicon, udailycal;
    String uid;
    String temp;
    private FirebaseAuth firebaseAuth;
    //database
    private DatabaseReference databaseRefUinfo;
    private Uri photoUrl;
    private StorageReference storageReference;
    private ProgressBar mProgressCircle;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
        setHasOptionsMenu(true);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), login.class));
        }
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();
                uid = profile.getUid();

                uname = profile.getDisplayName();
                photoUrl = profile.getPhotoUrl();
            }
        }

        //database reference
        databaseRefUinfo = FirebaseDatabase.getInstance().getReference(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference(user.getUid()).child("profile");

        retrieveData();
        mProgressCircle = view.findViewById(R.id.progress_circle);
        tvemail = view.findViewById(R.id.tvEmail);
        cImageView = view.findViewById(R.id.profileImg);
        tvname = view.findViewById(R.id.tvName);
        tvemail.setText(user.getEmail());
        tvname.setText(uname);

        Glide.with(getContext()).load(photoUrl)
                .centerCrop()
                .into(cImageView);

        mProgressCircle.setVisibility(View.INVISIBLE);
        /*BMI and body information*/
        tvgender = view.findViewById(R.id.tvGender);
        tvage = view.findViewById(R.id.tvAge);
        tvdob = view.findViewById(R.id.tvDob);


        /*Fab Buttons*/
        btnEditmode = view.findViewById(R.id.btn_editmode);
        btneditProfile = view.findViewById(R.id.btn_editprofile);
        btnAbout = view.findViewById(R.id.aboutbtn);

        btnEditmode.setOnClickListener(this);
        btneditProfile.setOnClickListener(this);
        btnAbout.setOnClickListener(this);

    }

    private void retrieveData() {
        databaseRefUinfo.child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        ugender = userInformation.getGender();
                        agee = userInformation.getAge();
                        udob = userInformation.getDob();

                        tvgender.setText(ugender);
                        tvage.setText(agee);
                        tvdob.setText(udob);
                    } catch (Exception e) {

                    }

                    storageReference.child(user.getUid());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //  Toast.makeText(getActivity(),"The read failed ", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onClick(View view) {
        if (view == btneditProfile) {
            try {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("image_url", photoUrl.toString());
                intent.putExtra("profile_name", uname);

                startActivity(intent);
                Toast.makeText(getActivity(), "Profile Editing", Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                Log.e("message", ex.getMessage());
            }
        } else if (view == btnEditmode) {
            Intent intent = new Intent(getActivity(), BodyInformation.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "Body Information", Toast.LENGTH_SHORT).show();

        } else if (view == btnAbout) {

            Intent intent = new Intent(getActivity(), About.class);
            startActivity(intent);

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else {
            switch (item.getItemId()) {
                case R.id.action_logout:
                    showAlertDialog();


                    return true;

            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();


    }
    private void logout() {
        firebaseAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(), login.class));

    }

}
