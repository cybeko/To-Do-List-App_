package com.example.proj_final.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.proj_final.data.Task;
import com.example.proj_final.data.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repo;
    private final LiveData<List<Task>> allTasks;
    public TaskViewModel(Application app) {
        super(app);
        repo = new TaskRepository(app);
        allTasks = repo.getAllTasks();
    }
    public LiveData<List<Task>> getAllTasks() { return allTasks; }
    public void insert(Task t) { repo.insert(t); }
    public void delete(Task task) {
        repo.delete(task);
    }
    public void update(Task task) {
        repo.update(task);
    }
    public void deleteAll() { repo.deleteAll(); }
}
