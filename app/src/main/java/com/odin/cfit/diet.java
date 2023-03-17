package com.odin.cfit;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odin.cfit.model.FoodDiary;
import com.odin.cfit.model.UserReqCalorie;

import java.text.DecimalFormat;
import java.util.Calendar;

/*import android.support.v4.app.Fragment;*/


public class diet extends Fragment{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser fUser;
    TextView tvdiet, tvCalorie, tvGuide, tvWeight_difference;
     ImageButton bt_toggle_text;
     Button bt_hide_text;
     View lyt_expand_text, lyt_diet_results;
     double required_Calories, converted_weight, uRequCal, weight_diff, uWeight_diff;
    EditText etweight, etgweight, etFood;
    //DecimalFormat df = new DecimalFormat("0.00");

    Spinner spinneFoodType;
    int day, month, year, hour, minute;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Diet");
        setHasOptionsMenu(true);

        //firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            this.getActivity().finish();
            startActivity(new Intent(getActivity(), login.class));
        }
        fUser = firebaseAuth.getCurrentUser();

        //database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(fUser.getUid());


       // df.setRoundingMode(RoundingMode.UP);

        bt_toggle_text = (ImageButton) view.findViewById(R.id.bt_toggle_text);
        bt_hide_text = (Button) view.findViewById(R.id.bt_hide_text);
        lyt_expand_text = (View) view.findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);

        lyt_diet_results =(View) view.findViewById(R.id.Lay_diet_results);
        lyt_diet_results.setVisibility(View.INVISIBLE);

        //intitialized
        tvdiet = (TextView) view.findViewById(R.id.dietfrag_txt);
        //tvdiet.setText("Click the Fab Button to calculate your required calorie to lose weight.");
        tvdiet.setText("About Eating For Weightloss");
        tvCalorie =(TextView) view.findViewById(R.id.tv_calorie);

        tvGuide = (TextView) view.findViewById(R.id.tv_guide);
        tvGuide.setText("Click on the menu button to calculate your required calories to lose weight");

        tvWeight_difference = (TextView) view.findViewById(R.id.tv_weighttoLose);

        initComponent();
        retieveData();
    }


    public void initComponent(){
        // text section


        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        bt_hide_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });
    }
    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                   // Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

//required Calories
    private void calculateRequiredCalories() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.required_cal_dialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        etweight = (EditText) dialog.findViewById(R.id.etweight);
        etgweight = (EditText) dialog.findViewById(R.id.etgweight);

        final Button btncancle = (Button) dialog.findViewById(R.id.btncancle);
        final Button btnsaveinfo = (Button) dialog.findViewById(R.id.btnsaveinfo);


        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnsaveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calc_required_calorie( Double.parseDouble(etweight.getText().toString().trim()),
                        Double.parseDouble(etgweight.getText().toString().trim()));

                /*
                etweight.setText("");
                etgweight.setText("");*/

                dialog.dismiss();
            }
        });
        // show it
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    //Method for calculating required calories
    public void calc_required_calorie(double current_weight, double goal_weight){

        weight_diff = current_weight - goal_weight;
        converted_weight = goal_weight * 2.2;
        required_Calories = converted_weight * 12;

        // Round to 2 decimal places
        DecimalFormat deciFormat = new DecimalFormat();
        deciFormat.setMaximumFractionDigits(2);
        String requiredCalories2Dec = deciFormat.format(required_Calories);

        // convert back to double
        required_Calories = Double.parseDouble(requiredCalories2Dec);


        Toast.makeText(getActivity(), converted_weight + " " + required_Calories + " " +"Calculate calories", Toast.LENGTH_SHORT).show();

        UserReqCalorie userReqCalorie = new UserReqCalorie(required_Calories, weight_diff);
        //firebase
        databaseReference.child("Required Calories").setValue(userReqCalorie);
    }
    //retrieve from database
   public void retieveData(){
       databaseReference.child("Required Calories").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   try{
                       tvGuide.setVisibility(View.INVISIBLE);
                       lyt_diet_results.setVisibility(View.VISIBLE);
                       UserReqCalorie userReqCalorie = snapshot.getValue(UserReqCalorie.class);

                       uRequCal = userReqCalorie.getRequiredCal();
                       uWeight_diff = userReqCalorie.getWeightDiff();
                       //set to textview
                       tvCalorie.setText(String.valueOf(uRequCal));
                       tvWeight_difference.setText(String.valueOf("-" +uWeight_diff));

                   } catch (Exception e) {
                       //throw new RuntimeException(e);
                       Toast.makeText(getActivity(), ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

                   }
               }else {
                   tvGuide.setVisibility(View.VISIBLE);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(getActivity(),"The read failed ", Toast.LENGTH_LONG).show();

           }
       });

   }

    public void logFood(){
        ArrayAdapter<CharSequence> adapter;
        final Dialog FLogdialog = new Dialog(getContext());
        FLogdialog.setContentView(R.layout.food_log_dialog);
        FLogdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(FLogdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        spinneFoodType = (Spinner) FLogdialog.findViewById(R.id.spinnerFoodType);

        etFood = (EditText) FLogdialog.findViewById(R.id.etfood);

        final TextView tvDate = (TextView) FLogdialog.findViewById(R.id.tvDate);
        final TextView tvTime = (TextView) FLogdialog.findViewById(R.id.tvTime);
        final Button btncancle = (Button) FLogdialog.findViewById(R.id.btncancle);
        final Button btnsaveinfo = (Button) FLogdialog.findViewById(R.id.btnsaveinfo);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.food_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneFoodType.setAdapter(adapter);
//        spinneFoodType.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getContext());

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tdate = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(dayOfMonth);
                        tvDate.setText(tdate);


                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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

                calc_required_calorie( Double.parseDouble(etweight.getText().toString().trim()),
                        Double.parseDouble(etgweight.getText().toString().trim()));
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
        databaseReference.child(fUser.getUid()).child("Food Diary").child(uploadCId).setValue(foodDiary);
    }

    /*menu Selection*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.diet_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_calculate) {
            calculateRequiredCalories();
        } else if (id == R.id.action_Logfood){
            logFood();
        }
        return super.onOptionsItemSelected(item);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diet, container, false);

        spinneFoodType = rootView.findViewById(R.id.spinnerFoodType);

        return rootView;
    }


}
