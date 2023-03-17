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

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.odin.cfit.AddReminderActivity;
import com.odin.cfit.R;
import com.odin.cfit.data.AlarmReminder;
import com.odin.cfit.data.ApplicationDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlarmReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ALARM_REMINDERS = "ALARM_REMINDERS";
    private Context mContext;
    private static final int REQUEST_CODE = 101;
    private int mNotificationId = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() == null || intent.getExtras().isEmpty()) return;
        mContext = context;

        String alarmReminderId = intent.getExtras().getString("alarmReceiverId");
        if (alarmReminderId == null || alarmReminderId.isEmpty()) return;

        AlarmReminder alarmReminder = ApplicationDatabase.getInstance(context)
                .alarmReminderDao().get(alarmReminderId).getValue();
        if (alarmReminder == null) return;

        createTravelNotifChannelIfNeeded();

        //Display a notification to view the task details
        Intent action = new Intent(context, AddReminderActivity.class);
        action.getExtras().putParcelable("alarmReminder", alarmReminder);
        PendingIntent operation = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification note = new NotificationCompat.Builder(context, CHANNEL_ALARM_REMINDERS)
                .setContentTitle("AlarmReminder")
                .setContentText(alarmReminder.title)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentIntent(operation)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true)
                .build();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            List<String> requiredPermissions = new ArrayList<>();
            requiredPermissions.add(Manifest.permission.POST_NOTIFICATIONS);
//            ActivityCompat.requestPermissions(context, requiredPermissions, REQUEST_CODE);
            return;
        }
        NotificationManagerCompat.from(context).notify(mNotificationId++, note);
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

}
