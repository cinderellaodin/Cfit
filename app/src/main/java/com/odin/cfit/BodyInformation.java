package com.odin.cfit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.odin.cfit.model.BodyMeasurement;
import com.odin.cfit.model.Goals;
import com.odin.cfit.model.UserInformation;

import java.text.DecimalFormat;
import java.util.Calendar;

public class BodyInformation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    //database
    private DatabaseReference databaseRefUinfo;
    final int REQUEST_CODE = 1;
    private Uri photoUrl;
    private StorageReference storageReference;
    FirebaseUser user;

    EditText etheight, etweight, etgweight;
    Spinner spinnerGender, spinnerMunits;
    RatingBar rb;
    TextView etdob, valuerb, tvage, tvbmi, tvbmicon, tvdailycal;
    double ratingvalue;
    String rvMsg = "";
    int day, month, year, yearfinal;
    Calendar calendar;

    double selectedValue;
    double[] values = {+5, -161};
    double height, weight, gweight, age, bmi, dailycal, weightforecast, ugoalcal, ubodysize,
            udressgoal, uweekgoal, bmr, uheight, uweight, ugweight,
    uchest, uarms, uwaist, ushoulder, uhip, ucalf, uthighs, uneck, uwrist;

    String msg = "";
    String uage = "";
    String ugender = "";
    String uname, agee, udob, uactivity, ubmi, ubmicon, udailycal;

    TextView tvname, tvemail, tvgender, tvdob, tvheight, tvweight, tvgweight, tvactivity,
    tvweeklygoal, tvchest, tvarms, tvshoulder, tvwaist, tvcalf, tvhip, tvthighs,tvsize,
            tvneck, tvwrist, tvgoalcal, forecastTv;

    String uid;
    String temp;
    DatePickerDialog datePickerDialog;

    double calDiff, lbsWeight, lbsweight_diff, rbmi, wfWeek, wfMonth;
    String ugoalcalS, weightforecastS;
    DecimalFormat deciFormat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_information);

        /*intitializing firebase*/
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            this.finish();
            startActivity(new Intent(this, login.class));
        }

        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();
               /* uid = profile.getUid();*/

                uname = profile.getDisplayName();
               /* photoUrl = profile.getPhotoUrl();*/
            }
        }
        setTitle(uname +"'s"+" " +"Body Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //database reference
        databaseRefUinfo = FirebaseDatabase.getInstance().getReference(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference(user.getUid()).child("profile");

       /* tvname = findViewById(R.id.tvChest);
        tvgender = findViewById(R.id.tvGender);
        tvage = findViewById(R.id.tvAge);
        tvdob = findViewById(R.id.tvDob);
        tvemail = findViewById(R.id.ghh);*/

        tvheight = findViewById(R.id.currentheight);
        tvweight = findViewById(R.id.currentweight);
        tvgweight = findViewById(R.id.goalweight);
        tvactivity = findViewById(R.id.activitylevel);
        tvweeklygoal = findViewById(R.id.weeklygoal);
        tvsize = findViewById(R.id.dresssize);

        /*Body Measurement*/
        tvchest = findViewById(R.id.chestmeasurement);
        tvarms = findViewById(R.id.armmeasurement);
        tvwaist = findViewById(R.id.waistmeasurement);
        tvshoulder = findViewById(R.id.shouldermeasurement);
        tvhip = findViewById(R.id.hipmeasurement);
        tvcalf = findViewById(R.id.calfmeasurement);
        tvthighs = findViewById(R.id.thighmeasurement);
        tvneck = findViewById(R.id.neckmeasurement);
        tvwrist = findViewById(R.id.wristmeasurement);

        /*calculation results*/

        tvbmi = findViewById(R.id.bmiresult);
        tvbmicon = findViewById(R.id.bmicondition);
        tvdailycal = findViewById(R.id.dailycalorie);
        tvgoalcal = findViewById(R.id.goalcalorie);
        forecastTv = findViewById(R.id.tvforecast);

        // Round to 2 decimal places
         deciFormat = new DecimalFormat();
        deciFormat.setMaximumFractionDigits(2);

        retrieveData();
        retrieveBodyData();
    }


    /*retrieve data*/
    private void retrieveData() {
        databaseRefUinfo.child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                       /* ugender = userInformation.getGender();
                        agee = userInformation.getAge();
                        udob = userInformation.getDob();*/


                        uheight = userInformation.getHeight();
                        uweight = userInformation.getWeight();
                        ugweight = userInformation.getGoalweight();
                        uactivity = userInformation.getActivity();
                        ubmi = userInformation.getBmi();
                        ubmicon = userInformation.getCondition();
                        udailycal = userInformation.getDailycalories();
                        ugoalcalS = userInformation.getUcaloriegoals();
                        weightforecastS = userInformation.getWeightforecasts();
                        String ftv = userInformation.getWeightforecasts();

                                // tvemail.setText(ubmicon);
                       /* tvgender.setText(ugender);
                        tvage.setText(agee);
                        tvdob.setText(udob);*/

                        tvbmi.setText(ubmi);
                        tvbmicon.setText(ubmicon);
                        tvdailycal.setText(udailycal);
                        tvgoalcal.setText(ugoalcalS);


                        tvheight.setText(String.valueOf(uheight));
                        tvweight.setText(String.valueOf(uweight));
                        tvgweight.setText(String.valueOf(ugweight));
                        tvactivity.setText(uactivity);

                        forecastTv.setText("With the information provided, with " +
                                "losing 1kg per week, you'll reach your goal weight on or during the period of " + weightforecastS);

                    } catch (Exception e) {
                        Toast.makeText(BodyInformation.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    storageReference.child(user.getUid());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BodyInformation.this,"The read failed ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retrieveBodyData() {
        databaseRefUinfo.child("Body Measurement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        BodyMeasurement bodyMeasurement = dataSnapshot.getValue(BodyMeasurement.class);
                       /* ugender = userInformation.getGender();
                        agee = userInformation.getAge();
                        udob = userInformation.getDob();*/


                        uchest = bodyMeasurement.getUserchest();
                        uarms = bodyMeasurement.getUserarm();
                        uwaist = bodyMeasurement.getUserwaist();
                       uthighs = bodyMeasurement.getUserthighs();
                        uhip = bodyMeasurement.getUserhip();
                        ushoulder= bodyMeasurement.getUsershoulder();
                        uneck = bodyMeasurement.getUserneck();
                        ucalf = bodyMeasurement.getUsercalf();
                        uwrist = bodyMeasurement.getUserwrist();
                        ubodysize = bodyMeasurement.getUserdsize();


                        tvchest.setText(String.valueOf(uchest));
                        tvarms.setText(String.valueOf(uarms));
                        tvwaist.setText(String.valueOf(uwaist));
                        tvthighs.setText(String.valueOf(uthighs));
                        tvhip.setText(String.valueOf(uhip));
                        tvcalf.setText(String.valueOf(ucalf));
                        tvshoulder.setText(String.valueOf(ushoulder));
                        tvthighs.setText(String.valueOf(uthighs));
                        tvneck.setText(String.valueOf(uneck));
                        tvwrist.setText(String.valueOf(uwrist));
                        tvsize.setText(String.valueOf(ubodysize));


                    } catch (Exception e) {
                        Toast.makeText(BodyInformation.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    storageReference.child(user.getUid());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BodyInformation.this,"The read failed ", Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
        if (id == R.id.update_bmi) {
            updatebmi();
        } else if ( id == R.id.update_measurement ){
           // Toast.makeText(this, "Update body measurement", Toast.LENGTH_SHORT).show();
            updatebodymeasurement();
        }else if (id == R.id.update_goals){
          //  Toast.makeText(this, "Update goals", Toast.LENGTH_SHORT).show();
            updatgoals();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatgoals() {

        final Dialog dialog = new Dialog(BodyInformation.this);
        dialog.setContentView(R.layout.goalsdialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button bt_submit = (Button) dialog.findViewById(R.id.updategoal);
        EditText etweeklyg =(EditText) dialog.findViewById(R.id.weekgoal);
        EditText etdressg =(EditText) dialog.findViewById(R.id.dressgoal);
       /* EditText etarm =(EditText) dialog.findViewById(R.id.armsM);
        EditText etneck =(EditText) dialog.findViewById(R.id.neckM);*/

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoals( Double.parseDouble(etweeklyg.getText().toString().trim()),
                        Double.parseDouble(etdressg.getText().toString().trim()));
                dialog.dismiss();
                Toast.makeText(BodyInformation.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });



        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void saveGoals(double uweekgoal, double udressgoal){
        Goals goals = new Goals(uweekgoal, udressgoal);

        databaseRefUinfo.child("Goals").setValue(goals);
    }

    private void updatebodymeasurement() {
        final Dialog dialog = new Dialog(BodyInformation.this);
        dialog.setContentView(R.layout.bodymeasuredialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button bt_submit = (Button) dialog.findViewById(R.id.btnsave);
        EditText etchest =(EditText) dialog.findViewById(R.id.chestM);
        EditText etshoulder =(EditText) dialog.findViewById(R.id.shouldersM);
        EditText etarm =(EditText) dialog.findViewById(R.id.armsM);
        EditText etneck =(EditText) dialog.findViewById(R.id.neckM);
        EditText etwaist =(EditText) dialog.findViewById(R.id.waistM);
        EditText ethip =(EditText) dialog.findViewById(R.id.hipM);
        EditText etcalf =(EditText) dialog.findViewById(R.id.calfM);
        EditText etwrist =(EditText) dialog.findViewById(R.id.WristM);
        EditText etthighs =(EditText) dialog.findViewById(R.id.thighsM);
        EditText etbSize =(EditText) dialog.findViewById(R.id.bodySize);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savebodyMeausrement(
                        Double.parseDouble(etchest.getText().toString().trim()),
                        Double.parseDouble(etshoulder.getText().toString().trim()),
                        Double.parseDouble(etarm.getText().toString().trim()),
                        Double.parseDouble(etneck.getText().toString().trim()),
                        Double.parseDouble(etwaist.getText().toString().trim()),
                        Double.parseDouble(ethip.getText().toString().trim()),
                        Double.parseDouble(etcalf.getText().toString().trim()),
                        Double.parseDouble(etwrist.getText().toString().trim()),
                        Double.parseDouble(etthighs.getText().toString().trim()),
                        Double.parseDouble(etbSize.getText().toString().trim())
                );

                dialog.dismiss();
                Toast.makeText(BodyInformation.this, "Body Measurement Saved", Toast.LENGTH_SHORT).show();
            }


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void savebodyMeausrement(double chest, double shoulder,
                                     double arm, double neck,
                                     double waist, double hip,
                                     double calf, double wrist,
                                     double thighs, double dressSize) {

        BodyMeasurement bdm = new BodyMeasurement(chest, shoulder, arm, neck, waist, hip,
                calf, wrist, thighs, dressSize );
        databaseRefUinfo.child("Body Measurement").setValue(bdm);

    }


    //Calling BMI Dialog
    private void updatebmi() {
        ArrayAdapter<CharSequence> adapter;
        LayoutInflater li = LayoutInflater.from(this);
        final View dialogView = li.inflate(R.layout.uinfodialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //  alertDialogBuilder.setTitle("Create Event");
        /*alertDialogBuilder.setIcon();*/
        alertDialogBuilder.setView(dialogView);
        //DDING WIDGET
        final AlertDialog alertDialog = alertDialogBuilder.create();

        spinnerGender = (Spinner) dialogView.findViewById(R.id.spinnerGender);
        /*spinnerMunits = (Spinner) dialogView.findViewById(R.id.spUnits);*/
        etdob = (TextView) dialogView.findViewById(R.id.etdob);
        etheight = (EditText) dialogView.findViewById(R.id.etheight);
        etweight = (EditText) dialogView.findViewById(R.id.etweight);
        etgweight = (EditText) dialogView.findViewById(R.id.etgweight);
        rb = (RatingBar) dialogView.findViewById(R.id.rb);

        valuerb = (TextView) dialogView.findViewById(R.id.valueRb);
        final Button btncancle = (Button) dialogView.findViewById(R.id.btncancle);
        final Button btnsaveinfo = (Button) dialogView.findViewById(R.id.btnsaveinfo);

        /*spinner adapters*/
        /*measurement unit*/
        /*adapter = ArrayAdapter.createFromResource(this, R.array.measurement_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMunits.setAdapter(adapter);
        spinnerMunits.setOnItemSelectedListener(this);
*/

        /*gender*/
        adapter = ArrayAdapter.createFromResource(this, R.array.gender_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        //rating bar

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 1) {
                    ratingvalue = 1.2;
                    rvMsg = "Little to no exercise";
                } else if (rating == 2) {
                    ratingvalue = 1.375;
                    rvMsg = "Light exercise (1-3 days per week )";
                } else if (rating == 3) {
                    ratingvalue = 1.55;
                    rvMsg = "Moderate exercise (3-5 days per week)";
                } else if (rating == 4) {
                    ratingvalue = 1.725;
                    rvMsg = "Heavy Exercise (6-7 days per week)";
                } else if (rating == 5) {
                    ratingvalue = 1.9;
                    rvMsg = "Very heavy exercise (twice pe rday)";
                } else if (rating == 0) {
                    ratingvalue = 0;
                    rvMsg = "you are required to rate your activity level";
                }
                valuerb.setText("For" + " " + rvMsg + " " + ratingvalue);
                /* tvactivity.setText("For"+" "+rvMsg+" "+ ratingvalue);*/
            }
        });

        /*date picker dialog*/
        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                //used for age
                yearfinal = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(BodyInformation.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int mont = month +1;
                        String tdate = String.valueOf(year) + "/" + String.valueOf(mont) + "/" + String.valueOf(dayOfMonth);
                        etdob.setText(tdate);

                        int yeardiff = yearfinal - year;
                        uage = String.valueOf(yeardiff);
                       // etdob.setText(uage);


                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });


        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnsaveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculatebmi();
                saveUserInformation(
                        spinnerGender.getSelectedItem().toString().trim(),
                        uage,
                        etdob.getText().toString().trim(),
                        valuerb.getText().toString().trim(),
                        ubmi,
                        ubmicon,
                        udailycal,
                        ugoalcalS,
                        weightforecastS,
                        Double.parseDouble(etheight.getText().toString().trim()),
                        Double.parseDouble(etweight.getText().toString().trim()),
                        Double.parseDouble(etgweight.getText().toString().trim())
                );

                /*etheight.setText("");
                etweight.setText("");
                etgweight.setText("");*/

                alertDialog.dismiss();
            }
        });
        // show it
        alertDialog.show();


    }

    private void calculatebmi() {
        weight = Double.parseDouble(etweight.getText().toString());
        gweight = Double.parseDouble(etgweight.getText().toString());
        height = Double.parseDouble(etheight.getText().toString());


        // String covntBmi = deciFormat.format(rbmi);

        //calculate required goal calories
        ugoalcal = 12 * gweight;
        ugoalcalS = deciFormat.format(ugoalcal);

        //calorie difference
         calDiff = 3500 - ugoalcal;

        //weightforecast
         lbsWeight = weight * 2.2 - gweight * 2.2;
         lbsweight_diff = lbsWeight * 3500;


       //( lbsweight_diff calories/pounds)
        weightforecast = lbsweight_diff / calDiff;
         wfWeek = weightforecast / 7;
         wfMonth = wfWeek/4;
        weightforecastS = String.valueOf(deciFormat.format(weightforecast)) + " Days" + String.valueOf(deciFormat.format(wfWeek)) + " Weeks";

        //BMI
        bmi = height / 100 * height / 100;
        rbmi = weight / bmi;

        //tvbmi.setText(String.valueOf(bmi));
        ubmi = String.valueOf(deciFormat.format(rbmi));
        if (rbmi <= 18.5) {
            msg = "Underweight :(";
        } else if (rbmi >= 18.5 && rbmi <= 24.9) {
            msg = "Normal weight:)";
        } else if (rbmi >= 25 && rbmi <= 29.9) {
            msg = "Overweight:(";
        } else if (rbmi >= 30) {
            msg = "Obese :(";
        }
        ubmicon = msg;
        //tvbmicon.setText(msg);

        //for bmr
        bmr = (10 * weight) + (6.25 * height) - (5 * age) + selectedValue;

        dailycal = bmr * (ratingvalue);

      //  tvdailycal.setText(String.valueOf("Your reqired daily calorie is:" + " " + dailycal));
        udailycal = String.valueOf("Your reqired daily calorie is:" + " " + deciFormat.format(dailycal));

    }


    private void saveUserInformation(String gender, String age, String dob, String activity, String bmi, String condition, String dailycalories,
                                     String ugoalcalS, String weightforecastS, double height, double weight, double goalweight) {

        UserInformation userInformation = new UserInformation(gender, age, dob, activity, bmi, condition, dailycalories, ugoalcalS, weightforecastS, height, weight,
                goalweight);

       // FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseRefUinfo.child("UserInfo").setValue(userInformation);

        Toast.makeText(this, "Saved....", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedValue = values[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}