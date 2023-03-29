package com.odin.cfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odin.cfit.adapter.FoodLogAdapter;
import com.odin.cfit.adapter.WeightTrackerAdapter;
import com.odin.cfit.model.FoodDiary;
import com.odin.cfit.model.UserReqCalorie;
import com.odin.cfit.model.WeighTracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FoodLogActivity extends AppCompatActivity {
    Spinner spinneFoodType;
    int day, month, year, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ArrayAdapter<CharSequence> adapter;
    EditText etFood;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser fUser;
    String foodtypeR, fooddR, foodtR;
    RecyclerView recyclerView;
    private FoodLogAdapter mfoodLogAdapter;
    List<FoodDiary> mFoodDiary;
    FloatingActionButton fb_foodDiary;
    Dialog FLogdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_log);
        //firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            this.finish();
            startActivity(new Intent(FoodLogActivity.this, login.class));
        }
        fUser = firebaseAuth.getCurrentUser();

        //database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(fUser.getUid());

        //float cation button
        fb_foodDiary = (FloatingActionButton) findViewById(R.id.fab_food_log);
        fb_foodDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logFood();
            }
        });

       // logFood();
        retrieveFoodLog();
    }

    private void retrieveFoodLog() {

        recyclerView = (RecyclerView) findViewById(R.id.fdrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        mFoodDiary = new ArrayList<>();
        mfoodLogAdapter = new FoodLogAdapter(FoodLogActivity.this, mFoodDiary);


        databaseReference.child("Food Diary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFoodDiary.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FoodDiary fd = ds.getValue(FoodDiary.class);
                    fd.setKey(ds.getKey());
                    mFoodDiary.add(fd);
                    if (mFoodDiary.size()<=0) {
                        Toast.makeText(FoodLogActivity.this, "No weights logged yet\n be sure to log your weight", Toast.LENGTH_SHORT).show();

                    }else{
                        recyclerView.setAdapter(mfoodLogAdapter);
                        // Toast.makeText(getActivity(), "weight"+ weights, Toast.LENGTH_SHORT).show();

                    }
                }

                mfoodLogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodLogActivity.this,"The read failed ", Toast.LENGTH_LONG).show();

            }
        });
    }


    public void logFood(){

        FLogdialog = new Dialog(FoodLogActivity.this);
        FLogdialog.setContentView(R.layout.food_log_dialog);
        FLogdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(FLogdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        spinneFoodType = (Spinner) FLogdialog.findViewById(R.id.spinnerFoodType);

        etFood = (EditText) FLogdialog.findViewById(R.id.etfood);
        final ImageButton img_date = (ImageButton) FLogdialog.findViewById(R.id.imgDate);
        final ImageButton img_time = (ImageButton) FLogdialog.findViewById(R.id.imgTime);

        final TextView tvDate = (TextView) FLogdialog.findViewById(R.id.tvDate);
        final TextView tvTime = (TextView) FLogdialog.findViewById(R.id.tvTime);
        final Button btncancle = (Button) FLogdialog.findViewById(R.id.btncancle);
        final Button btnsaveinfo = (Button) FLogdialog.findViewById(R.id.btnsaveinfo);

        adapter = ArrayAdapter.createFromResource(FoodLogActivity.this, R.array.food_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneFoodType.setAdapter(adapter);
//        spinneFoodType.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getContext());

        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(FoodLogActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                       int mont = month +1;
                        String tdate = String.valueOf(year) + "/" + String.valueOf(mont) + "/" + String.valueOf(dayOfMonth);
                        tvDate.setText(tdate);


                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        img_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(FoodLogActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });


        //buttons
        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FLogdialog.dismiss();
            }
        });

        btnsaveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                food_diary_entry(
                        spinneFoodType.getSelectedItem().toString().trim(),
                        etFood.getText().toString().trim(),
                        tvDate.getText().toString().trim(),
                        tvTime.getText().toString().trim()

                );
                /*
                etweight.setText("");
                etgweight.setText("");*/

                FLogdialog.dismiss();
            }
        });
        // show it
        FLogdialog.show();
        FLogdialog.getWindow().setAttributes(lp);
    }

    public void food_diary_entry(String foodType, String foodDetails, String tvDate, String tvTime){

        FoodDiary foodDiary = new FoodDiary(foodType, foodDetails, tvDate, tvTime);
        String uploadCId = databaseReference.push().getKey();
        databaseReference.child("Food Diary").child(uploadCId).setValue(foodDiary);
    }
}