package com.odin.cfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odin.cfit.adapter.WPlanTrackerAdapter;
import com.odin.cfit.model.WeighTracker;
import com.odin.cfit.model.WorkoutPlans;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlansActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    FirebaseUser fUser;
    TextView tvGuide;
    RecyclerView recyclerView;
    private WPlanTrackerAdapter mWPlanTrackerAdapter;
    List<WorkoutPlans> mWorkoutPlans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plans);
        setTitle("Workout Plans");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            this.finish();
            startActivity(new Intent(this, login.class));
        }
        fUser = firebaseAuth.getCurrentUser();

        //database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(fUser.getUid());


        recyclerView = (RecyclerView) findViewById(R.id.rv_workoutPlans);
        tvGuide = (TextView) findViewById(R.id.tv_guide);
        tvGuide.setText("Click on the Float button to create a workout plan");


        //to retrieve workout plans
       // retrieveWorkoutPlans();
    }

    private void retrieveWorkoutPlans() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mWorkoutPlans = new ArrayList<>();
        mWPlanTrackerAdapter = new WPlanTrackerAdapter(this, mWorkoutPlans);


        mDBListener =  databaseReference.child(fUser.getUid()).child("Workout Plans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mWorkoutPlans.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    WorkoutPlans workoutPlans = ds.getValue(WorkoutPlans.class);
                    workoutPlans.setKey(ds.getKey());
                    mWorkoutPlans.add(workoutPlans);
                    if (mWorkoutPlans.size()<=0) {
                      //  Toast.makeText(WorkoutPlansActivity.this, "No weights logged yet\n be sure to log your weight", Toast.LENGTH_SHORT).show();
                        tvGuide.setVisibility(View.VISIBLE);
                    }else{
                        recyclerView.setAdapter(mWPlanTrackerAdapter);
                        tvGuide.setVisibility(View.VISIBLE);
                        Toast.makeText(WorkoutPlansActivity.this, "No weights logged yet\n be sure to log your weight", Toast.LENGTH_SHORT).show();

                    }
                }
                mWPlanTrackerAdapter.notifyDataSetChanged();

                //mProgressCircle.setVisibility(View.INVISIBLE);

              /*  mWorkoutPlans.clear();

                }*/

                mWPlanTrackerAdapter.notifyDataSetChanged();

                // mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WorkoutPlansActivity.this,"The read failed ", Toast.LENGTH_LONG).show();

            }
        });
        mWPlanTrackerAdapter.setOnItemClickListener(new WPlanTrackerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final String selectedKey;

        /*        WorkoutPlans selected = mWorkoutPlans.get(position);
                selectedKey = selected.getKey();

                databaseReference.child(fUser.getUid()).child("Workout Plans").child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getContext(),"Item Deleted" , Toast.LENGTH_SHORT).show();

                    }
                });
                mWPlanTrackerAdapter.notifyDataSetChanged();*/
                // mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });


    }

}