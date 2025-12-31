package com.example.proj_final.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private final TaskDao dao;
    private final LiveData<List<Task>> allTasks;
    public TaskRepository(Application app) {
        TaskDatabase db = TaskDatabase.getDatabase(app);
        dao = db.taskDao();
        allTasks = dao.getAllTasks();
    }
    public LiveData<List<Task>> getAllTasks() { return allTasks; }

    public void insert(Task t) { new Thread(() -> dao.insert(t)).start(); }
    public void delete(Task task) {
        new Thread(() -> dao.delete(task)).start();
    }
    public void update(Task task) {
        new Thread(() -> dao.update(task)).start();
    }
    public void deleteAll() { new Thread(dao::deleteAll).start(); }
}
