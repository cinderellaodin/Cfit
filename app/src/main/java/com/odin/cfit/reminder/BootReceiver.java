package com.odin.cfit.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.odin.cfit.data.AlarmReminder;
import com.odin.cfit.data.ApplicationDatabase;

import java.util.List;

/**
 * A Broadcast Receiver with the intent of receiving device broadcasts on reboot
 * Use this to restart services which may not survive device reboot and need to be started again.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // reschedule existing alarms

        AlarmReminderScheduler scheduler = AlarmReminderScheduler.getInstance();

        List<AlarmReminder> reminders = ApplicationDatabase.getInstance(context)
                .alarmReminderDao().getAll().getValue();
        if (reminders != null) {
            reminders.forEach(reminder -> {
                scheduler.setAlarm(context, reminder);
            });
        }
    }

}
