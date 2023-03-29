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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class diet extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser fUser;
    TextView tvdiet, tvCalorie, tvGuide, tvWeight_difference;
     double required_Calories, converted_weight, weight_diff;
     String uRequCal, uWeight_diff;
    EditText etweight, etgweight;
    //DecimalFormat df = new DecimalFormat("0.00");

    FloatingActionButton fab_options, fab_addFood,fab_reqCal, fab_foodlog, fab_foodTips, fab_calc;

    private View parent_view;
    private View back_drop;
    private boolean rotate = false;
    private View lyt_tape;
    private View lyt_scale;



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



        tvCalorie =(TextView) view.findViewById(R.id.tv_calorie);

      /*  tvGuide = (TextView) view.findViewById(R.id.tv_guide);
        tvGuide.setText("Click on the menu button to calculate your required calories to lose weight");
*/
        tvWeight_difference = (TextView) view.findViewById(R.id.tv_weighttoLose);

        retieveData();

        /*for fab menu*/
        parent_view = view.findViewById(android.R.id.content);
        back_drop = view.findViewById(R.id.back_drop);

        lyt_tape = view.findViewById(R.id.lyt_tape);
        lyt_scale = view.findViewById(R.id.lyt_scale);
        ViewAnimation.initShowOut(lyt_tape);
        ViewAnimation.initShowOut(lyt_scale);
        back_drop.setVisibility(View.GONE);

        fab_options = (FloatingActionButton) view.findViewById(R.id.fab_food_options);
        fab_addFood = (FloatingActionButton) view.findViewById(R.id.fab_food_log);
        fab_reqCal = (FloatingActionButton) view.findViewById(R.id.fab_Req_cal);


        fab_foodlog = (FloatingActionButton) view.findViewById(R.id.btn_log_food);
        fab_foodTips = (FloatingActionButton) view.findViewById(R.id.btn_viewDTips);
        fab_calc = (FloatingActionButton) view.findViewById(R.id.calcbtn);

        fab_options.setOnClickListener(this);
        fab_reqCal.setOnClickListener(this);
        fab_addFood.setOnClickListener(this);
        fab_foodlog.setOnClickListener(this);
        fab_foodTips.setOnClickListener(this);
        fab_calc.setOnClickListener(this);

        fab_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMode(view);
            }
        });
        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMode(fab_options);
            }
        });

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
        String Wdiff = deciFormat.format(weight_diff);

        // convert back to double
       // required_Calories = Double.parseDouble(requiredCalories2Dec);
      //String req_Calories = requiredCalories2Dec;



        Toast.makeText(getActivity(), Wdiff + " " + requiredCalories2Dec + " " +"Calculate calories", Toast.LENGTH_SHORT).show();

        UserReqCalorie userReqCalorie = new UserReqCalorie(requiredCalories2Dec, Wdiff);
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




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diet, container, false);

        return rootView;
    }


    @Override
    public void onClick(View v) {
         if (v == fab_addFood) {
             startActivity(new Intent(getContext(), FoodLogActivity.class));
        } else if (v == fab_reqCal) {
             calculateRequiredCalories();
         } else if (v == fab_foodlog) {
            startActivity(new Intent(getContext(), FoodLogActivity.class));
        }else if (v == fab_foodTips) {
            //startActivity(new Intent(getContext(), workout.class));
        }else if (v == fab_calc) {
            startActivity(new Intent(getContext(), BodyInformation.class));
        }
    }
}
