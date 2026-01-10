package com.example.proj_final.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotificationTimeRepository {

    private final NotificationTimeDao dao;
    private final LiveData<List<NotificationTime>> allTimes;

    public NotificationTimeRepository(Application app) {
        TaskDatabase db = TaskDatabase.getDatabase(app);
        dao = db.notificationTimeDao();
        allTimes = dao.getAllTimesLive();
    }
    public LiveData<List<NotificationTime>> getAllTimes() {
        return allTimes;
    }
    public List<NotificationTime> getAllTimesSync() {
        return dao.getAllTimes();
    }
    public void insert(NotificationTime time) {
        new Thread(() -> dao.insert(time)).start();
    }

    public void delete(NotificationTime time) {
        new Thread(() -> dao.delete(time)).start();
    }

}
