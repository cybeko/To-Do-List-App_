package com.example.proj_final.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_final.R;
import com.example.proj_final.data.NotificationTime;

import java.util.ArrayList;
import java.util.List;

public class NotificationTimeAdapter extends RecyclerView.Adapter<NotificationTimeAdapter.TimeViewHolder> {
    private final List<NotificationTime> times = new ArrayList<>();
    private TimeClickListener timeClickListener;
    private DeleteClickListener deleteClickListener;
    public interface TimeClickListener {
        void onTimeClick(NotificationTime time);
    }
    public interface DeleteClickListener {
        void onDeleteClick(NotificationTime time);
    }
    public void setTimeClickListener(TimeClickListener listener) {
        this.timeClickListener = listener;
    }
    public void setDeleteClickListener(DeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void setTimes(List<NotificationTime> newTimes) {
        times.clear();
        if (newTimes != null) times.addAll(newTimes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        NotificationTime time = times.get(position);

        holder.tvTime.setText(
                String.format("%02d:%02d", time.hour, time.minute)
        );

        holder.tvTime.setOnClickListener(v -> {
            if (timeClickListener != null) {
                timeClickListener.onTimeClick(time);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(time);
            }
        });
    }

    @Override
    public int getItemCount() {
        return times.size();
    }
    static class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        ImageButton btnDelete;
        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnDelete = itemView.findViewById(R.id.btnDeleteTime);
        }
    }
}
