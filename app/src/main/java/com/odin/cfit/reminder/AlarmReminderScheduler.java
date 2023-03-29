package com.odin.cfit.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.odin.cfit.data.AlarmReminder;

import java.util.Calendar;

public class AlarmReminderScheduler {

    private static AlarmReminderScheduler INSTANCE;
    private static final String LOG_TAG = AlarmReminderScheduler.class.getSimpleName();

    public void setAlarm(Context context, AlarmReminder reminder) {
        unscheduleAlarmReminder(context, reminder);
        Intent alarmIntent = new Intent(context, AlarmReminderReceiver.class);

        Log.d(LOG_TAG, "Setting alarm: " +  reminder.getTime().toString());
        Log.d(LOG_TAG, "Alarm ID: " +  reminder.getId());

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
        unscheduleAlarmReminder(context, reminder);
        Intent alarmIntent = new Intent(context, AlarmReminderReceiver.class);

        // generate an action id that would always be same. This allows us to replace/delete existing
        // intents
        String actionId = "alarmReminder" + "_" + reminder.getId();
        Log.d(LOG_TAG, "Setting repeating alarm: " +  reminder.getTime().toString());
        Log.d(LOG_TAG, "Alarm ID: " +  reminder.getId());

        alarmIntent.setAction(actionId);
        alarmIntent.addCategory("alarmReminder");
        alarmIntent.putExtra("alarmReminderId", reminder.getId());

        // let's use the hasCode of the travel's id as the request code, so that subsequent calls override this
        int requestCode = actionId.hashCode();
        long repeatInterval = 0;
        if (reminder.repeat) {
            switch (reminder.getRepeatType()) {
                case MINUTE:
                    repeatInterval = 1000 * 60;
                    break;
                case HOUR:
                    repeatInterval = 1000 * 60 * 60;
                    break;
                case DAY:
                    repeatInterval = 1000 * 60 * 60 * 24;
                    break;
                case WEEK:
                    repeatInterval = 1000 * 60 * 60 * 60 * 7;
                    break;
                case MONTH:
                    repeatInterval = 100 * 60 * 60 * 7 * 30;
                    break;
            }
        }

        if (reminder.getRepeatNo() > 0) {
            repeatInterval = repeatInterval * reminder.getRepeatNo();
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (repeatInterval > 0) {
            mgr.setRepeating(AlarmManager.RTC_WAKEUP, reminder.getTime().getTime(), repeatInterval, pendingIntent);
        } else {
            mgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.getTime().getTime(), pendingIntent);
        }

    }

    /**
     * Un-schedules any existing reminder alarms.
     */
    public void unscheduleAlarmReminder(Context context, AlarmReminder reminder) {
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
