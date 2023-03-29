package com.odin.cfit.reminder;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.odin.cfit.AddReminderActivity;
import com.odin.cfit.R;
import com.odin.cfit.data.AlarmReminder;
import com.odin.cfit.data.ApplicationDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AlarmReminderReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = AlarmReminderReceiver.class.getSimpleName();
    private static final String CHANNEL_ALARM_REMINDERS = "ALARM_REMINDERS";
    private Context mContext;
    private static final int REQUEST_CODE = 101;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Received alarm reminder");
        mContext = context;

        String alarmReminderId = intent.getExtras().getString("alarmReminderId");
        Log.d(LOG_TAG, "Reminder id: " + alarmReminderId);

        if (alarmReminderId == null || alarmReminderId.isEmpty()) return;
        Log.d(LOG_TAG, "Alarm reminder id: " + alarmReminderId);

        Disposable disposable = ApplicationDatabase.getInstance(context)
                .alarmReminderDao().getRx(alarmReminderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(alarmReminder -> {
                    issueReminderNotification(context, alarmReminder);
                    if (!alarmReminder.repeat) {
                        AlarmReminderScheduler.getInstance().unscheduleAlarmReminder(context, alarmReminder);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.d(LOG_TAG, "Error issuing reminder notification: " + throwable.getMessage());
                });
        disposables.add(disposable);

    }

    private void createTravelNotifChannelIfNeeded() {
        // Make a channel if necessary
        CharSequence channelName = mContext.getResources().getString(R.string.text_alarm_reminder_channel_title);
        String channelDescription = mContext.getResources().getString(R.string.text_alarm_reminder_channel_desc);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ALARM_REMINDERS, channelName, importance);
        channel.setDescription(channelDescription);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void issueReminderNotification(Context context, @Nullable AlarmReminder alarmReminder) {
        Log.d(LOG_TAG, "Issuing reminder notification: " + alarmReminder);
        if (alarmReminder == null) return;

        Log.d(LOG_TAG, "Issuing reminder notification at: " + alarmReminder.getTime());

        Log.d(LOG_TAG, "Creating notification channel if needed");
        createTravelNotifChannelIfNeeded();

        //Display a notification to view the task details
        Intent action = new Intent(context, AddReminderActivity.class);
        action.putExtra("alarmReminder", alarmReminder);
        PendingIntent operation = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String actionId = "alarmReminder" + "_" + alarmReminder.getId();

        Notification note = new NotificationCompat.Builder(context, CHANNEL_ALARM_REMINDERS)
                .setContentTitle("AlarmReminder")
                .setContentText(alarmReminder.title)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentIntent(operation)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .setAutoCancel(true)
                .build();

        Log.d(LOG_TAG, "Showing reminder");

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(LOG_TAG, "Need permissions for notifications");
            return;
        }
        NotificationManagerCompat.from(context).notify(actionId.hashCode(), note);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disposables.dispose();
        disposables = null;
    }
}
