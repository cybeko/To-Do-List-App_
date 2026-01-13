package com.example.proj_final.ui.task;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.proj_final.data.task.Task;
import com.example.proj_final.data.task.TaskRepository;

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
    public LiveData<List<Task>> getTasksByPriority() {
        return repo.getTasksByPriority();
    }
    public LiveData<List<Task>> getTasksByName() {
        return repo.getTasksByName();
    }
    public LiveData<List<Task>> getTasksByNewest() {
        return repo.getTasksByNewest();
    }
    public void insert(Task t) { repo.insert(t); }
    public void delete(Task task) {
        repo.delete(task);
    }
    public void update(Task task) {
        repo.update(task);
    }
    public void deleteAll() { repo.deleteAll(); }
}
