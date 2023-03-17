package com.odin.cfit.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.odin.cfit.data.AlarmReminder;

public class AlarmReminderScheduler {

    private static AlarmReminderScheduler INSTANCE;

    public void setAlarm(Context context, AlarmReminder reminder) {
        Intent alarmIntent = new Intent(context, AlarmReminderReceiver.class);

        // generate an action id that would always be same. This allows us to replace/delete existing
        // intents
        String actionId = "alarmReminder" + "_" + reminder.getId();

        alarmIntent.setAction(actionId);
        alarmIntent.addCategory("alarmReminder");
        alarmIntent.putExtra("alarmReminderId", reminder.getId());

        // let's use the hasCode of the travel's id as the request code, so that subsequent calls override this
        int requestCode = actionId.hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        mgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.getTime().getTime(), pendingIntent);

    }

    public void setRepeatAlarm(Context context, AlarmReminder reminder) {
        Intent alarmIntent = new Intent(context, AlarmReminderReceiver.class);

        // generate an action id that would always be same. This allows us to replace/delete existing
        // intents
        String actionId = "alarmReminder" + "_" + reminder.getId();

        alarmIntent.setAction(actionId);
        alarmIntent.addCategory("alarmReminder");
        alarmIntent.putExtra("alarmReminderId", reminder.getId());

        // let's use the hasCode of the travel's id as the request code, so that subsequent calls override this
        int requestCode = actionId.hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        mgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.getTime().getTime(), pendingIntent);

    }

    /**
     * Un-schedules any existing reminder alarms.
     */
    public void unscheduleTravelReminders(Context context, AlarmReminder reminder) {
        Intent alarmIntent = new Intent(context, AlarmReminderReceiver.class);

        String actionId = "alarmReminder" + "_" + reminder.getId();

        alarmIntent.setAction(actionId);
        alarmIntent.addCategory("alarmReminder");
        alarmIntent.putExtra("alarmReminderId", reminder.getId());

        // let's use the hasCode of the travel's id as the request code, so that subsequent calls override this
        int requestCode = actionId.hashCode();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }

    public static AlarmReminderScheduler getInstance(){
        if(INSTANCE == null)
            INSTANCE = new AlarmReminderScheduler();
        return INSTANCE;
    }

}
