package com.odin.cfit.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.odin.cfit.R;
import com.odin.cfit.data.AlarmReminderContract;

public class AlarmCursorAdapter extends CursorAdapter {
    private TextView mTitleText,mDateAndTimeText, mRepeatInfoText;
    private ImageView mActiveImage, mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

   // private TextDrawableHelper mDrawableBuilder;

    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_items, viewGroup, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mTitleText = (TextView) view.findViewById(R.id.recycle_title);
        mDateAndTimeText = (TextView) view.findViewById(R.id.recycle_date_time);
        mRepeatInfoText = (TextView) view.findViewById(R.id.recycle_repeat_info);
        mActiveImage = (ImageView) view.findViewById(R.id.active_image);
        mThumbnailImage = (ImageView) view.findViewById(R.id.thumbnail_image);


        int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
        int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
        int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
        int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
        int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
        int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);


        String title = cursor.getString(titleColumnIndex);
        String date = cursor.getString(dateColumnIndex);
        String time = cursor.getString(timeColumnIndex);
        String repeat = cursor.getString(repeatColumnIndex);
        String repeatNo = cursor.getString(repeatNoColumnIndex);
        String repeatType = cursor.getString(repeatTypeColumnIndex);
        String active = cursor.getString(activeColumnIndex);

        String datetime = date + " " + time;


        setReminderTitle(title);
        setReminderDateTime(datetime);
        setReminderRepeatInfo(repeat, repeatNo, repeatType);
        setActiveImage(active);

        if (date != null){
            String dateTime = date + " " + time;
            setReminderDateTime(dateTime);
        }else {
            mDateAndTimeText.setText("Date Not Set");

        }if (repeat != null){
            setReminderRepeatInfo(repeat, repeatNo, repeatType);
        }else {
            mRepeatInfoText.setText("Repeat Not Set");
        }
        if (active != null){
            setActiveImage(active);
        }else{
            mActiveImage.setImageResource(R.drawable.ic_baseline_notifications_off_24);
        }
    }

    public void setReminderTitle(String title){
        mTitleText.setText(title);
        String letter = "A";

        if (title !=null && !title.isEmpty()){
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        //create a circular icon consisting of a random background color and first letter
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }

    //set date and time views
    public void setReminderDateTime(String dateTime){
        mDateAndTimeText.setText(dateTime);
    }

    //set repeat views
    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType){
        if (repeat.equals("true")){
            mRepeatInfoText.setText("Every " + repeat + " " + repeatType + "(s)");
        }else if (repeat.equals("false")){
            mRepeatInfoText.setText("Repeat Off");
        }
    }

    //set active image as on or off
    public void setActiveImage(String active){
        if (active.equals("true")){
            mActiveImage.setImageResource(R.drawable.ic_baseline_notifications_24);
        }else if (active.equals("false")){
            mActiveImage.setImageResource(R.drawable.ic_baseline_notifications_off_24);
        }
    }
}
