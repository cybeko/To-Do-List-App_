package com.example.proj_final.ui.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proj_final.data.notification.NotificationTime;
import com.example.proj_final.data.notification.NotificationTimeRepository;

import java.util.List;

public class NotificationTimeViewModel extends AndroidViewModel {

    private final NotificationTimeRepository repo;
    private final LiveData<List<NotificationTime>> allTimes;

    public NotificationTimeViewModel(@NonNull Application app) {
        super(app);
        repo = new NotificationTimeRepository(app);
        allTimes = repo.getAllTimes();
    }
    public LiveData<List<NotificationTime>> getAllTimes() {
        return allTimes;
    }

    public void insert(NotificationTime time) {
        repo.insert(time);
    }

    public void delete(NotificationTime time) {
        repo.delete(time);
    }


}