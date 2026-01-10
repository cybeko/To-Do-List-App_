package com.example.proj_final;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.example.proj_final.data.NotificationTime;
import com.example.proj_final.ui.NotificationTimeAdapter;
import com.example.proj_final.ui.NotificationTimeViewModel;
import com.example.proj_final.worker.DailyQuoteScheduler;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class NotificationTimesActivity extends AppCompatActivity {
    private NotificationTimeViewModel vm;
    private NotificationTimeAdapter adapter;
    private RecyclerView rvTimes;
    private TextView tvEmpty;
    private ImageButton btnAddTime;
    private MaterialButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_times);

        rvTimes = findViewById(R.id.llNotificationTimes);
        tvEmpty = findViewById(R.id.tvEmptyMessage);
        btnAddTime = findViewById(R.id.btnAddTime);
        btnBack = findViewById(R.id.btnBack);

        vm = new ViewModelProvider(this).get(NotificationTimeViewModel.class);

        setupRecyclerView();
        observeTimes();
        setupAddButton();
        setupBackButton();
    }
    private void setupRecyclerView() {
        adapter = new NotificationTimeAdapter();
        rvTimes.setLayoutManager(new LinearLayoutManager(this));
        rvTimes.setAdapter(adapter);

        adapter.setDeleteClickListener(time -> {
            vm.delete(time);

            String workName = "quote_" + time.hour + "_" + time.minute;
            WorkManager.getInstance(this).cancelUniqueWork(workName);
        });
    }

    private void observeTimes() {
        vm.getAllTimes().observe(this, times -> {
            adapter.setTimes(times);
            if (times == null || times.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvTimes.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvTimes.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }
    private void setupAddButton() {
        btnAddTime.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);

            TimePickerDialog picker = new TimePickerDialog(
                    this,
                    (view, selectedHour, selectedMinute) -> {
                        NotificationTime time = new NotificationTime();
                        time.hour = selectedHour;
                        time.minute = selectedMinute;
                        vm.insert(time);

                        DailyQuoteScheduler.scheduleQuotes(
                                this,
                                selectedHour,
                                selectedMinute,
                                "quote_" + selectedHour + "_" + selectedMinute
                        );
                    },
                    hour,
                    minute,
                    true
            );
            picker.show();
        });
    }
}
