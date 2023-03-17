package com.odin.cfit.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.odin.cfit.data.AlarmReminder;

import java.util.List;

@Dao
public interface AlarmReminderDao {

    @Query("SELECT * FROM alarmreminder")
    public LiveData<List<AlarmReminder>> getAll();

    @Query("SELECT * FROM alarmreminder WHERE ID IS :id")
    public LiveData<AlarmReminder> get(String id);

    @Query("SELECT * FROM alarmreminder WHERE id IN (:ids)")
    public LiveData<List<AlarmReminder>> loadAllByIds(int[] ids);

    @Query("SELECT * FROM alarmreminder WHERE title LIKE :title LIMIT 1")
    public LiveData<AlarmReminder> findByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(AlarmReminder reminder);

    @Delete
    public int delete(AlarmReminder reminder);

}
