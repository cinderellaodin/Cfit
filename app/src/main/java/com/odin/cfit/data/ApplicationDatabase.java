package com.odin.cfit.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.odin.cfit.data.dao.AlarmReminderDao;

@Database(entities = {AlarmReminder.class}, version = 1)
@TypeConverters({ApplicationTypeConverters.class})
public abstract class ApplicationDatabase extends RoomDatabase {

    private static ApplicationDatabase INSTANCE;

    public static ApplicationDatabase getInstance(Context context) {
        // if we've initialized a global instance already, return it.
        if (INSTANCE != null) return INSTANCE;

        // initialize global instance
        INSTANCE = Room.databaseBuilder(context, ApplicationDatabase.class, "cfit-database")
                .build();
        return INSTANCE;
    }

    // DAO definitions

    public abstract AlarmReminderDao alarmReminderDao();

}