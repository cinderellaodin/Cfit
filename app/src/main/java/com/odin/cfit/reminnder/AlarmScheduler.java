package com.odin.cfit.reminnder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.odin.cfit.data.AlarmReminderProvider;

public class AlarmScheduler {

    public void setAlarm(Context applicationContext, long selectedTimestamp, Uri mCurrentReminderUri) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(applicationContext);

        PendingIntent operation =
                ReminderAlarmService.getReminderPedingIntent(applicationContext, mCurrentReminderUri);
        if (Build.VERSION.SDK_INT >= 33){
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedTimestamp, operation);
        }else if (Build.VERSION.SDK_INT >= 28){
            manager.setExact(AlarmManager.RTC_WAKEUP,selectedTimestamp, operation);
        }else{
            manager.set(AlarmManager.RTC_WAKEUP, selectedTimestamp, operation);
        }
    }

    public void setRepeatAlarm(Context applicationContext, long selectedTimestamp, Uri mCurrentReminderUri, long mRepeatTime) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(applicationContext);
        PendingIntent operation =
                ReminderAlarmService.getReminderPedingIntent(applicationContext, mCurrentReminderUri);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, selectedTimestamp, mRepeatTime, operation);

    }

    public void cancelAlarm(Context context, Uri reminderTask){
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPedingIntent(context, reminderTask);
        manager.cancel(operation);
    }
}
