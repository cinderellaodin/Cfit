package com.odin.cfit.reminder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.odin.cfit.R;
import com.odin.cfit.data.AlarmReminder;

import java.text.SimpleDateFormat;

public class AlarmReminderViewHolder extends RecyclerView.ViewHolder {

    TextView recycler_title;
    TextView recycler_dateTime;
    TextView recycler_repeat;

    ImageView imageView;
    ImageView mThumbnailImage;
    ColorGenerator mColorGenerator = com.amulyakhare.textdrawable.util.ColorGenerator.DEFAULT;
    Drawable mDrawableBuilder;

    public AlarmReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        recycler_title = itemView.findViewById(R.id.recycle_title);
        recycler_dateTime = itemView.findViewById(R.id.recycle_date_time);
        recycler_repeat = itemView.findViewById(R.id.recycle_repeat_info);
        imageView = itemView.findViewById(R.id.active_image);
        mThumbnailImage = itemView.findViewById(R.id.thumbnail_image);
    }

    public void bind(AlarmReminder reminder) {
        recycler_title.setText(reminder.getTitle());
        recycler_repeat.setText(reminder.getTime().toString());
        if (reminder.getActive()){
            imageView.setImageResource(R.drawable.ic_baseline_notifications_24);
        }else {
            imageView.setImageResource(R.drawable.ic_baseline_notifications_off_24);
        }
        String reminderType = "";
        switch (reminder.getRepeatType()) {
            case HOUR:
                reminderType = "Every " + reminder.getRepeatNo() + " hour(s)";
                break;
            case DAY:
                reminderType = "Every " + reminder.getRepeatNo() + " day(s)";
                break;
            case WEEK:
                reminderType = "Every " + reminder.getRepeatNo() + " week(s)";
                break;
            case MONTH:
                reminderType = "Every " + reminder.getRepeatNo() + " month(s)";
                break;
            case MINUTE:
                reminderType = "Every " + reminder.getRepeatNo() + " minute(s)";
                break;
            case NONE:
                reminderType = "No repeat";
                break;
         }
         if (!reminder.getActive()) {
             reminderType = "Alarm off";
         }
        recycler_repeat.setText(reminderType);
        int color = mColorGenerator.getRandomColor();

        String letter = "A";
        if (reminder.getTitle() !=null && !reminder.getTitle().isEmpty()){
            letter = reminder.getTitle().substring(0, 1);
        }
        //create a circular icon consisting of a random background color and first letter
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
        recycler_dateTime.setText(SimpleDateFormat.getDateTimeInstance().format(reminder.getTime()));
    }
}
