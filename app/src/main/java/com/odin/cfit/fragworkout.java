package com.odin.cfit;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odin.cfit.adapter.WPlanTrackerAdapter;
import com.odin.cfit.adapter.WeightTrackerAdapter;
import com.odin.cfit.model.WeighTracker;
import com.odin.cfit.model.WorkoutPlans;

import java.util.ArrayList;
import java.util.List;

/*import android.support.v4.app.Fragment;*/


public class fragworkout extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    FirebaseUser fUser;
    TextView tvGuide;
    FloatingActionButton fabPlan, fabViewPlans, fabTips, fabCalc;
    EditText etPlanName;



    Intent intent;

    String planName, ExerciseName;
    double exReps, exSets, exRest, exDuration;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Workout");

        //firebase initialization
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            this.getActivity().finish();
            startActivity(new Intent(getActivity(), login.class));
        }
        fUser = firebaseAuth.getCurrentUser();

        //database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(fUser.getUid());

       /* tvGuide = (TextView) view.findViewById(R.id.tv_guide);
        tvGuide.setText("Click on the Float button to create a workout plan");
*/

        fabPlan = (FloatingActionButton) view.findViewById(R.id.fab_workout_plan);
        fabViewPlans = (FloatingActionButton) view.findViewById(R.id.btn_View_plans);
        fabTips = (FloatingActionButton) view.findViewById(R.id.btn_viewTips);
        fabCalc = (FloatingActionButton) view.findViewById(R.id.calcbtn);

        fabPlan.setOnClickListener(this);
        fabViewPlans.setOnClickListener(this);
        fabTips.setOnClickListener(this);
        fabCalc.setOnClickListener(this);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }




    @Override
    public void onClick(View v) {
        if (v == fabPlan){
            SetPlanName();
        } else if (v == fabViewPlans) {
            startActivity(new Intent(getContext(), WorkoutPlansActivity.class));
        }else if (v == fabTips) {
        //startActivity(new Intent(getContext(), workout.class));
        }else if (v == fabCalc) {
            startActivity(new Intent(getContext(), BodyInformation.class));
        }
    }

    private void SetPlanName() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.plan_name_dialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        etPlanName = (EditText) dialog.findViewById(R.id.et_PlanName);


        final Button btncancle = (Button) dialog.findViewById(R.id.btncancle);
        final Button btnNext = (Button) dialog.findViewById(R.id.btn_move_plan);


        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    passTextName(etPlanName.getText().toString().trim());


                dialog.dismiss();
            }
        });
        // show it
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void passTextName(String trim) {

        intent = new Intent(getActivity(), workout.class);
        intent.putExtra("plan_name", trim);
        startActivity(intent);
        Toast.makeText(getActivity(), ""+ trim, Toast.LENGTH_LONG).show();
    }


}
