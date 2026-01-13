package com.example.proj_final.data.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();
    @Query("SELECT * FROM tasks ORDER BY title ASC")
    LiveData<List<Task>> getTasksByName();
    @Query("SELECT * FROM tasks ORDER BY priority DESC")
    LiveData<List<Task>> getTasksByPriority();
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    LiveData<List<Task>> getTasksByNewest();

    @Query("DELETE FROM tasks")
    void deleteAll();
    @Update
    void update(Task task);
    @Delete
    void delete(Task task);
}
