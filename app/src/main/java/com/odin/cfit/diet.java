package com.odin.cfit;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.odin.cfit.model.UserReqCalorie;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/*import android.support.v4.app.Fragment;*/


public class diet extends Fragment {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser fUser;
    TextView tvdiet, tvCalorie, tvGuide, tvWeight_difference;
     ImageButton bt_toggle_text;
     Button bt_hide_text;
     View lyt_expand_text, lyt_diet_results;
     double required_Calories, converted_weight, uRequCal, weight_diff, uWeight_diff;
    EditText etweight, etgweight;
    //DecimalFormat df = new DecimalFormat("0.00");
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
        return inflater.inflate(R.layout.fragment_diet, container, false);
    }

}
