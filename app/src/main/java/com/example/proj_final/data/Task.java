package com.example.proj_final.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long _id;
    public String title;
    public String description;
    public int priority;
    public long deadline;
    public boolean isCompleted;

    public Task() {}
    public Task(String title, String description, int priority, long deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.isCompleted = false;
    }

    public Task(String title, String description, int priority, long deadline, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public long getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @NonNull
    @Override
    public String toString() {
        String status = isCompleted ? "✓" : "✗";
        String deadlineStr = (deadline > 0)
                ? " | " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(deadline)
                : "";
        return "[" + status + "] " + title +
                " (priority " + priority + ")" +
                deadlineStr;
    }
}
