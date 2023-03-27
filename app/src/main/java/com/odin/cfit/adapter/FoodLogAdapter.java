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
import com.odin.cfit.model.FoodDiary;
import com.odin.cfit.model.WeighTracker;

import java.util.List;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.ViewHolder> {
    private Context mContext;
    private List<FoodDiary> mFoodDiary;
    private FoodLogAdapter.OnItemClickListener mListener;

    public FoodLogAdapter(FragmentActivity activity, List<FoodDiary> mFoodDiary) {
        this.mContext = activity;
        this.mFoodDiary = mFoodDiary;
    }


    @NonNull
    @Override
    public FoodLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_foodlog_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodLogAdapter.ViewHolder holder, int position) {
        FoodDiary foodDiary = mFoodDiary.get(position);
            String foodT = foodDiary.getFoodtype();
            holder.FoodType.setText(foodT);

          //  holder.WeightTitle.setText((int) weighTrackerC.getProgweight());
        holder.FoodloggedDate.setText(foodDiary.getEntrydate() + " " + foodDiary.getEntrytime());
    }

    @Override
    public int getItemCount() {
        return mFoodDiary.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView FoodType;
        public TextView FoodloggedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            FoodType = itemView.findViewById(R.id.tvfoodType);
            FoodloggedDate = itemView.findViewById(R.id.tv_foodloggedDate);

            itemView.setOnClickListener(this);

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

    public void setOnItemClickListener(FoodLogAdapter.OnItemClickListener listener){
        mListener = (OnItemClickListener) listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
