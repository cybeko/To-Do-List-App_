package com.example.proj_final.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj_final.data.Task;
import com.example.proj_final.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.VH> {
    private List<Task> data = new ArrayList<>();
    private OnTaskClickListener listener;
    private OnTaskStatusChangedListener adapterListener;
    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }
    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }
    public void setTasks(List<Task> list) {
        data = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new VH(v);
    }
    public interface OnTaskStatusChangedListener {
        void onTaskStatusChanged(Task task);
    }
    public void setOnTaskStatusChangedListener(OnTaskStatusChangedListener l) {
        adapterListener = l;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int pos) {
        Task task = data.get(pos);

        Context context = holder.itemView.getContext();

        // Title
        holder.tv.setText(task.getTitle());

        // Status
        holder.cb.setChecked(task.isCompleted);

        // Description
        String descriptionText = task.getDescription().isEmpty()
                ? context.getString(R.string.none)
                : task.getDescription();
        holder.tvDescription.setText(
                context.getString(R.string.description_format,
                        context.getString(R.string.description),
                        descriptionText)
        );

        // Priority
        holder.tvPriority.setText(
                context.getString(R.string.priority_format,
                        context.getString(R.string.priority),
                        String.valueOf(task.getPriority()))
        );

        // Deadline
        String deadlineText;
        if (task.getDeadline() > 0) {
            Date deadlineDate = new Date(task.getDeadline());
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateTimeInstance(
                    java.text.DateFormat.SHORT,
                    java.text.DateFormat.SHORT,
                    context.getResources().getConfiguration().locale
            );

            deadlineText = dateFormat.format(deadlineDate);
        } else {
            deadlineText = context.getString(R.string.none);
        }

        holder.tvDeadline.setText(
                context.getString(R.string.deadline_format,
                        context.getString(R.string.deadline),
                        deadlineText)
        );
        holder.tvDeadline.setVisibility(View.VISIBLE);

        // checkbox
        holder.cb.setOnClickListener(v -> {
            task.isCompleted = holder.cb.isChecked();
            if (adapterListener != null) {
                adapterListener.onTaskStatusChanged(task);
            }
        });

        // expand card
        holder.ivExpand.setOnClickListener(v -> {
            boolean isExpanded = holder.expandableLayout.getVisibility() == View.VISIBLE;

            if (isExpanded) {
                collapse(holder.expandableLayout);
                holder.ivExpand.animate().rotation(0f).setDuration(250).start();
            } else {
                expand(holder.expandableLayout);
                holder.ivExpand.animate().rotation(180f).setDuration(250).start();
            }
        });

        // card click
        holder.itemView.setOnClickListener(v -> {
            if (v != holder.cb && v != holder.ivExpand && listener != null) {
                listener.onTaskClick(task);
            }
        });
    }
    private void expand(View v) {
        v.setVisibility(View.VISIBLE);
        v.measure(
                View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.requestLayout();

        ValueAnimator animator = ValueAnimator.ofInt(0, targetHeight);
        animator.addUpdateListener(animation -> {
            v.getLayoutParams().height = (int) animation.getAnimatedValue();
            v.requestLayout();
        });
        animator.setDuration(250);
        animator.start();
    }

    private void collapse(View v) {
        int initialHeight = v.getMeasuredHeight();

        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, 0);
        animator.addUpdateListener(animation -> {
            v.getLayoutParams().height = (int) animation.getAnimatedValue();
            v.requestLayout();
        });

        animator.setDuration(250);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    static class VH extends RecyclerView.ViewHolder {
        TextView tv, tvDescription, tvDeadline, tvPriority;
        CheckBox cb;
        ImageView ivExpand;
        LinearLayout expandableLayout;
        VH(View v) {
            super(v);
            tv = v.findViewById(R.id.tvTitle);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvDeadline = v.findViewById(R.id.tvDeadline);
            tvPriority = v.findViewById(R.id.tvPriority);
            cb = v.findViewById(R.id.cbCompleted);
            ivExpand = v.findViewById(R.id.ivExpand);
            expandableLayout = v.findViewById(R.id.expandableLayout);
        }
    }

}
