package com.example.proj_final.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proj_final.data.notification.NotificationTime;
import com.example.proj_final.data.notification.NotificationTimeDao;
import com.example.proj_final.data.task.Task;
import com.example.proj_final.data.task.TaskDao;

@Database(entities = {Task.class, NotificationTime.class}, version = 5, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract NotificationTimeDao notificationTimeDao();
    private static volatile TaskDatabase INSTANCE;
    public static TaskDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskDatabase.class, "task_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
