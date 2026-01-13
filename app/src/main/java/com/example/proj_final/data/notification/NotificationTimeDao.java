package com.example.proj_final.data.notification;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationTimeDao {

    @Query("SELECT * FROM notification_times")
    List<NotificationTime> getAllTimes();

    @Insert
    void insert(NotificationTime time);

    @Delete
    void delete(NotificationTime time);

    @Query("SELECT * FROM notification_times")
    LiveData<List<NotificationTime>> getAllTimesLive();
}
