package com.example.proj_final;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.proj_final.data.Task;
import com.example.proj_final.ui.TaskViewModel;

import java.util.Calendar;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {
    private TaskViewModel viewModel;
    private EditText etTitle, etDescription, etPriority, etDeadline;
    private CheckBox cbDeadline;
    private Button btnSave;
    private boolean isCompleted = false;
    private Calendar deadlineCalendar = Calendar.getInstance();
    private long taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> finish());

        initViews();
        initViewModel();
        setupDeadlineCheckbox();
        setupDeadlinePicker();
        setupSaveButton();
    }
    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPriority = findViewById(R.id.etPriority);
        cbDeadline = findViewById(R.id.cbDeadline);
        etDeadline = findViewById(R.id.etDeadline);
        btnSave = findViewById(R.id.btnSave);

        handleIntent();
    }
    private void handleIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("taskId")) {
            taskId = intent.getLongExtra("taskId", -1);
            btnSave.setText(R.string.update);

            etTitle.setText(intent.getStringExtra("taskTitle"));
            etDescription.setText(intent.getStringExtra("taskDescription"));
            etPriority.setText(String.valueOf(intent.getIntExtra("taskPriority", 1)));
            isCompleted = intent.getBooleanExtra("taskCompleted", false);

            long deadlineMillis = intent.getLongExtra("taskDeadline", 0);
            if (deadlineMillis > 0) {
                cbDeadline.setChecked(true);
                deadlineCalendar.setTimeInMillis(deadlineMillis);
                etDeadline.setVisibility(EditText.VISIBLE);

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(deadlineMillis);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                String formattedDateTime = this.getString(
                        R.string.date_time_format,
                        day,
                        month,
                        year,
                        hour,
                        minute
                );
                etDeadline.setText(formattedDateTime);
            }
        } else {
            btnSave.setText(R.string.create);
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(TaskViewModel.class);
    }
    private void setupDeadlineCheckbox() {
        cbDeadline.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etDeadline.setVisibility(EditText.VISIBLE);
            } else {
                etDeadline.setVisibility(EditText.GONE);
                etDeadline.setText("");
            }
        });
    }
    private void setupDeadlinePicker() {
        etDeadline.setOnClickListener(v -> pickDateTime());
    }
    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> saveTask());
    }
    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String prio = etPriority.getText().toString().trim();
        long deadline = cbDeadline.isChecked() ? deadlineCalendar.getTimeInMillis() : 0;

        if (!title.isEmpty() && !prio.isEmpty()) {
            int priority = Integer.parseInt(prio);
            String description = desc.isEmpty() ? "" : desc;
            Task task = new Task(title, description, priority, deadline, isCompleted);

            if (taskId != -1) {
                task._id = taskId;
                task.isCompleted = isCompleted;
                viewModel.update(task);
            } else {
                viewModel.insert(task);
            }
            finish();
        } else {
            Toast.makeText(this,
                    getString(R.string.enter_title_priority),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    private void pickDateTime() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    deadlineCalendar.set(year, month, dayOfMonth);
                    TimePickerDialog tpd = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                deadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                deadlineCalendar.set(Calendar.MINUTE, minute);
                                String formattedDateTime = getString(
                                        R.string.date_time_format,
                                        dayOfMonth,
                                        month + 1,
                                        year,
                                        hourOfDay,
                                        minute
                                );
                                etDeadline.setText(formattedDateTime);
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true);
                    tpd.show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
}
