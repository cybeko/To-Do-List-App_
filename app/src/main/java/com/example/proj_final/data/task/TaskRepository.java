package com.example.proj_final.data.task;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.proj_final.data.database.TaskDatabase;

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
    public LiveData<List<Task>> getTasksByPriority() { return dao.getTasksByPriority(); }
    public LiveData<List<Task>> getTasksByName() {
        return dao.getTasksByName();
    }
    public LiveData<List<Task>> getTasksByNewest() {
        return dao.getTasksByNewest();
    }
    public void insert(Task t) { new Thread(() -> dao.insert(t)).start(); }
    public void delete(Task task) {
        new Thread(() -> dao.delete(task)).start();
    }
    public void update(Task task) {
        new Thread(() -> dao.update(task)).start();
    }
    public void deleteAll() { new Thread(dao::deleteAll).start(); }
}
