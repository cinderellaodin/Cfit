package com.odin.cfit.reminnder;

import android.app.AlarmManager;
import android.content.Context;

public class AlarmManagerProvider {
   private static final String TAG = AlarmManagerProvider.class.getSimpleName();
   private static AlarmManager sAlarmManager;

   public static synchronized void injectAlarmManager(AlarmManager alarmManager){
       if (sAlarmManager != null){
           throw new IllegalArgumentException("Alarm Manager Already Set");
       }
       sAlarmManager = alarmManager;
   }

    /*package*/ static synchronized AlarmManager getAlarmManager(Context applicationContext) {
        if (sAlarmManager == null){
            sAlarmManager = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
        }
        return sAlarmManager;
    }
}
