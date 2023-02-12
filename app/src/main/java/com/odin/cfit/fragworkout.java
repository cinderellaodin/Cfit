package com.odin.cfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.odin.cfit.R;

/*import android.support.v4.app.Fragment;*/


public class fragworkout extends Fragment {

TextView tvwork;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Workout");


        tvwork = (TextView) view.findViewById(R.id.workout_txt);
        tvwork.setText("Select your prefered work out");

        
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

}
