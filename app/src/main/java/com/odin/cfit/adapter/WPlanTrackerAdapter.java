package com.odin.cfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.odin.cfit.R;
import com.odin.cfit.model.WorkoutPlans;

import java.util.List;

public class WPlanTrackerAdapter extends RecyclerView.Adapter<WPlanTrackerAdapter.ViewHolder> {
    private Context mContext;

    List<WorkoutPlans> mWorkoutPlans;

    private WPlanTrackerAdapter.OnItemClickListener mListener;

    public WPlanTrackerAdapter(FragmentActivity activity, List<WorkoutPlans> mWorkoutPlans){
        this.mContext = activity;
        this.mWorkoutPlans = mWorkoutPlans;
    }

    @NonNull
    @Override
    public WPlanTrackerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_plan_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WPlanTrackerAdapter.ViewHolder holder, int position) {
            WorkoutPlans workoutPlans = mWorkoutPlans.get(position);

        String pName = workoutPlans.getPlanName();
        holder.planName.setText(pName);


    }

    @Override
    public int getItemCount() {
        return mWorkoutPlans.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView planName;

        public ViewHolder(View v) {
            super(v);
            planName = v.findViewById(R.id.PlanName);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //passing clicks and position
            if (mListener != null){
                //check if listener is connected
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION){
                    //send clickback to activity
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public void setOnItemClickListener(WPlanTrackerAdapter.OnItemClickListener onItemClickListener) {
        mListener = (OnItemClickListener) onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
