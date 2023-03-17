package com.odin.cfit.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.odin.cfit.R;
import com.odin.cfit.data.AlarmReminder;
import com.odin.cfit.data.AlarmReminderContract;
import com.odin.cfit.reminder.AlarmReminderViewHolder;

import java.util.List;

public class AlarmCursorAdapter extends RecyclerView.Adapter<AlarmReminderViewHolder> {
    private TextView mTitleText,mDateAndTimeText, mRepeatInfoText;
    private ImageView mActiveImage, mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    private List<AlarmReminder> items;
    public OnItemCLickListener onItemCLickListener;

    @NonNull
    @Override
    public AlarmReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlarmReminderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmReminderViewHolder holder, int position) {
        AlarmReminder item = this.items.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            int realPosition = holder.getAbsoluteAdapterPosition();
            AlarmReminder realItem = items.get(realPosition);
            onItemCLickListener.onClick(realItem);
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        else {
            return items.size();
        }
    }

    public void setItems(List<AlarmReminder> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnItemCLickListener(OnItemCLickListener onItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener;
    }

    public interface OnItemCLickListener {
        public void onClick(AlarmReminder reminder);
    }

}
