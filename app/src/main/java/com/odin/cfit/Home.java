package com.odin.cfit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    TextView email,name;
    private FirebaseAuth firebaseAuth;
    private Uri photoUrl;
    FirebaseUser user;


    String uid, uname;
    CircularImageView cImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*firebase*/
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, login.class));
        }
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // String providerId = profile.getProviderId();
                uid = profile.getUid();
                uname = profile.getDisplayName();
                photoUrl = profile.getPhotoUrl();
            }
        }


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);


        View headerView = navigationView.getHeaderView(0);
        email = headerView.findViewById(R.id.txtvemail);
        cImageView = headerView.findViewById(R.id.navimageView);
        name = headerView.findViewById(R.id.txtvname);

        email.setText(user.getEmail());
        name.setText(uname);

        try {
            if (photoUrl == null) {
                cImageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher_round));
            } else {

                Glide.with(this).load(photoUrl)
                        .centerCrop()
                        .into(cImageView);
            }
        } catch (Exception ex) {
            Log.e("Error here", ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
       /*     Snackbar.make(this, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
            super.onBackPressed();
        }
    }


    private void displaySelectedScreen(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                fragment = new homm();
                break;
            case R.id.nav_profile:
                fragment = new profile();
                break;
            case R.id.nav_progress_report:
                fragment = new progress_report();
                break;
            case R.id.nav_workout:
                finish();
                startActivity(new Intent(this, workout.class));
                break;
            case R.id.nav_food:
                fragment = new diet();
                break;
            case R.id.nav_reminder:
                fragment = new reminder();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_layout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }
}
