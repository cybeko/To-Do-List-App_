package com.example.proj_final;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_final.data.NotificationTime;
import com.example.proj_final.data.Quote;
import com.example.proj_final.network.QuoteApi;
import com.example.proj_final.ui.NotificationTimeViewModel;
import com.example.proj_final.ui.QuoteNotifier;
import com.example.proj_final.ui.TaskAdapter;
import com.example.proj_final.ui.TaskViewModel;

import android.view.View;
import android.widget.Toast;

import com.example.proj_final.worker.DailyQuoteScheduler;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.appcompat.app.AlertDialog;

import com.example.proj_final.data.Task;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel viewModel;
    private final TaskAdapter adapter = new TaskAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askNotificationPermission();

        setupStatusBar();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupCreateButton();

        scheduleAllNotificationTimes();
    }
    private void scheduleAllNotificationTimes() {
        NotificationTimeViewModel vm = new ViewModelProvider(this).get(NotificationTimeViewModel.class);

        vm.getAllTimes().observe(this, times -> {
            if (times == null || times.isEmpty()) return;

            for (NotificationTime t : times) {
                DailyQuoteScheduler.scheduleQuotes(
                        MainActivity.this,
                        t.hour,
                        t.minute,
                        "quote_" + t.hour + "_" + t.minute
                );
            }
        });
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
    private void restoreLanguage() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isUkr = prefs.getBoolean("isUkrainian", false);

        Locale locale = isUkr ? new Locale("uk") : Locale.ENGLISH;
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    private void toggleLanguage() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isUkr = prefs.getBoolean("isUkrainian", false);
        isUkr = !isUkr;

        prefs.edit().putBoolean("isUkrainian", isUkr).apply();

        Locale locale = isUkr ? new Locale("uk") : Locale.ENGLISH;
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        String message = getString(R.string.language_switched);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        recreate();
    }
    private void setupToolbar() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_translate) {
                toggleLanguage();
                return true;
            } else if (id == R.id.action_filter) {
                View filterView = topAppBar.findViewById(R.id.action_filter);
                showFilterMenu(filterView);
                return true;
            }else if (id == R.id.action_quote) {
                fetchRandomQuote();
                return true;
            }
            else if (id == R.id.action_notifications) {
                startActivity(new Intent(this, NotificationTimesActivity.class));
                return true;
            }
            return false;
        });
    }
    private void fetchRandomQuote() {
        QuoteApi.fetchRandomQuote(new QuoteApi.QuoteCallback() {
            @Override
            public void onSuccess(Quote quote) {
                runOnUiThread(() ->
                        QuoteNotifier.showQuoteNotification(MainActivity.this, quote.text, quote.author)
                );
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(
                                MainActivity.this,
                                getString(R.string.failed_load_quote),
                                Toast.LENGTH_SHORT
                        ).show()
                );
            }
        });
    }
    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                       PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }
    private void setupStatusBar() {
        Window window = getWindow();
        int color = ContextCompat.getColor(this, R.color.bgStatusBar);
        window.setStatusBarColor(color);
        window.getDecorView().setSystemUiVisibility(0);
    }
    private void showFilterMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.filter_name) {
                viewModel.getTasksByName().observe(this, adapter::setTasks);
                return true;
            } else if (id == R.id.filter_priority) {
                viewModel.getTasksByPriority().observe(this, adapter::setTasks);
                return true;
            } else if (id == R.id.filter_recent) {
                viewModel.getTasksByNewest().observe(this, adapter::setTasks);
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
                .setTitle(getString(R.string.delete_task_title))
                .setMessage(getString(R.string.delete_task_message, task.title))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    viewModel.delete(task);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
