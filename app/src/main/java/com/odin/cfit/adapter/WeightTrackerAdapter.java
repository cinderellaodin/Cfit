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
import com.odin.cfit.model.WeighTracker;


import java.util.List;

public class WeightTrackerAdapter extends RecyclerView.Adapter<WeightTrackerAdapter.ViewHolder> {
    private Context mContext;
    private List<WeighTracker> mWeighTracker;
    private WeightTrackerAdapter.OnItemClickListener mListener;

    public WeightTrackerAdapter(FragmentActivity activity, List<WeighTracker> mWeighTracker) {
        this.mContext = activity;
        this.mWeighTracker = mWeighTracker;
    }


    @NonNull
    @Override
    public WeightTrackerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_weight_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightTrackerAdapter.ViewHolder holder, int position) {
            WeighTracker weighTrackerC = mWeighTracker.get(position);

            Double weight = weighTrackerC.getProgweight();
            holder.WeightTitle.setText(String.valueOf(weight) + "Kg");

          //  holder.WeightTitle.setText((int) weighTrackerC.getProgweight());
        holder.loggedDate.setText(weighTrackerC.getProgDate());
    }

    @Override
    public int getItemCount() {
        return mWeighTracker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView WeightTitle;
        public TextView loggedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            WeightTitle = itemView.findViewById(R.id.weightText);
            loggedDate = itemView.findViewById(R.id.loggedDate);

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

    public void setOnItemClickListener(FitNewsAdapter.OnItemClickListener listener){
        mListener = (OnItemClickListener) listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
