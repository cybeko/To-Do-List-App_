package com.example.proj_final.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_times")
public class NotificationTime {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int hour;
    public int minute;
    public NotificationTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
    public NotificationTime() {
    }
}
