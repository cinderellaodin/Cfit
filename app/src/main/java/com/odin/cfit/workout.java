package com.odin.cfit;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odin.cfit.model.WorkoutPlans;

import java.util.Calendar;

public class workout extends AppCompatActivity {

    String newWorkout_PlanName, WPname;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    FirebaseUser fUser;
    Button btncancel, btnsave;
    TextView tvlabel;
    EditText etExercisename, etExercisereps, etExercisesets, etExerciserest, etExerciseduration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        if (getIntent().hasExtra("plan_name")){
            WPname = getIntent().getStringExtra("plan_name");

            /* getSupportActionBar().setTitle(WPname);*/
        }
        getSupportActionBar().setTitle( WPname + "Plan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            this.finish();
            startActivity(new Intent(workout.this, login.class));
        }
        fUser = firebaseAuth.getCurrentUser();

        //database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        btncancel = (Button) findViewById(R.id.btncancle);
        btnsave = (Button) findViewById(R.id.btn_save_plan);

        etExercisename = (EditText) findViewById(R.id.etExerciseName);
        etExercisereps = (EditText) findViewById(R.id.etExerciseReps);
        etExercisesets = (EditText) findViewById(R.id.etExerciseSets);
        etExerciserest = (EditText) findViewById(R.id.etExerciseRest);
        etExerciseduration = (EditText) findViewById(R.id.etExerciseDuration);
        tvlabel = (TextView) findViewById(R.id.wlabel);

        tvlabel.setText(WPname);


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveWorkoutPlan(
                        WPname,
                        etExercisename.getText().toString().trim(),
                        Double.parseDouble(etExercisereps.getText().toString().trim()),
                        Double.parseDouble(etExercisesets.getText().toString().trim()),
                        Double.parseDouble(etExerciserest.getText().toString().trim()),
                        Double.parseDouble(etExerciseduration.getText().toString().trim())
                );
            }
        });
            }




    private void saveWorkoutPlan(String planNmae, String ExerciseName, double Ereps, double Esets, double Erest, double Eduration ){
        String cc = "12/13/2023";
        WorkoutPlans workoutPlans = new WorkoutPlans(cc, planNmae, ExerciseName, Ereps, Esets, Erest, Eduration);

       String uploadId = databaseReference.push().getKey();
        databaseReference.child(fUser.getUid()).child("Workout Plans").child(uploadId).setValue(workoutPlans);
        addnotification(planNmae);
        Toast.makeText(workout.this, planNmae+" event has been added. ", Toast.LENGTH_SHORT).show();


    }

    public void addnotification(String WOplan){
        //first use the notificationcompact.builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.fitness)
                .setContentTitle("Tips")
                .setContentText("You Just Created  " + WOplan + " Plan")
                .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("You Just created a plan......\n Click the event to check out Dressing tips"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
       /* Intent notificationIntent = new Intent(this, Event.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0 , notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/
        builder.setAutoCancel(true);
        builder.setColor(Color.parseColor("#FF80AB"));
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
     //   builder.setSound(Uri.parse("android.resource://"+ getApplicationContext().getPackageName()+"/"+R.raw.bark));

        //add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
