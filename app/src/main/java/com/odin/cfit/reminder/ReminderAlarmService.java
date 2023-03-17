package com.odin.cfit.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.odin.cfit.AddReminderActivity;
import com.odin.cfit.R;
import com.odin.cfit.data.AlarmReminderContract;

public class ReminderAlarmService extends IntentService {
  private static final String TAG = ReminderAlarmService.class.getSimpleName();

  private static final int NOTIFICATION_ID = 42;

  //this is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPedingIntent(Context applicationContext, Uri mCurrentReminderUri) {
        Intent action = new Intent(applicationContext, ReminderAlarmService.class);
        action.setData(mCurrentReminderUri);
        return PendingIntent.getService(applicationContext, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Display a notification to view the task details
        Intent action = new Intent(this, AddReminderActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String description= "";
        try{
            if (cursor != null && cursor.moveToFirst()){
                description = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            }
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }

        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle("AlarmReminder")
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentIntent(operation)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);
    }


}
