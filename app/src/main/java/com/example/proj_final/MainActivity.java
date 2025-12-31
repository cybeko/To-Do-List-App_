package com.example.proj_final;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proj_final.ui.TaskAdapter;
import com.example.proj_final.ui.TaskViewModel;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.appcompat.app.AlertDialog;
import com.example.proj_final.data.Task;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel viewModel;
    private final TaskAdapter adapter = new TaskAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupStatusBar();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupCreateButton();
    }
    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(TaskViewModel.class);

        viewModel.getAllTasks().observe(this, adapter::setTasks);
    }
    private void setupCreateButton() {
        Button btnCreate = findViewById(R.id.btnCreateTask);
        btnCreate.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CreateTaskActivity.class))
        );
    }
    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnTaskClickListener(this::showTaskOptionsDialog);
        adapter.setOnTaskStatusChangedListener(task -> viewModel.update(task));
    }
    private void setupToolbar() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_translate) {
                Toast.makeText(this, "translate clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_filter) {
                View filterView = topAppBar.findViewById(R.id.action_filter);
                showFilterMenu(filterView);
                return true;
            }
            return false;
        });
    }
    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#212121"));
            window.getDecorView().setSystemUiVisibility(0);
        }
    }
    private void showFilterMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.filter_name) {
                Toast.makeText(this, "Filter by name", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.filter_priority) {
                Toast.makeText(this, "Filter by priority", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.filter_recent) {
                Toast.makeText(this, "Filter by last created", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popup.show();
    }
    private void showTaskOptionsDialog(Task task) {
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_task_options, null);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(task.title)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnEditTask).setOnClickListener(v -> {
            dialog.dismiss();

            Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
            intent.putExtra("taskId", task._id);
            intent.putExtra("taskTitle", task.title);
            intent.putExtra("taskDescription", task.description);
            intent.putExtra("taskPriority", task.priority);
            intent.putExtra("taskDeadline", task.deadline);
            intent.putExtra("taskCompleted", task.isCompleted);
            startActivity(intent);
        });

        dialogView.findViewById(R.id.btnDeleteTask).setOnClickListener(v -> {
            dialog.dismiss();
            showDeleteConfirmDialog(task);
        });

        dialog.show();
    }
    private void showDeleteConfirmDialog(Task task) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Видалити задачу")
                .setMessage("Ви впевнені, що хочете видалити задачу:\n\n\""
                        + task.title + "\"\n\nНазавжди?")
                .setPositiveButton("Видалити", (dialog, which) -> {
                    viewModel.delete(task);
                })
                .setNegativeButton("Скасувати", null)
                .show();
    }
}
