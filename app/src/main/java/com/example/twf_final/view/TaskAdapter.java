package com.example.twf_final.view;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskEntity> tasks;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TaskAdapter(Context context, List<TaskEntity> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    public void updateTasks(List<TaskEntity> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }




    public TaskEntity getTaskAt(int position) {
        if (tasks != null && position >= 0 && position < tasks.size()) {
            return tasks.get(position);
        }
        return null;
    }

    public void removeTaskAt(int position) {
        tasks.remove(position);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntity currentTask = tasks.get(position);
        holder.taskName.setText(currentTask.getTaskName());
        holder.subjectName.setText(currentTask.getSubjectName());
        holder.status.setText(currentTask.getStatus());
        holder.creationDate.setText("Created: " + currentTask.getCreationDate());
        holder.dueDate.setText("Due: " + currentTask.getDueDate());
        holder.dueTime.setText(currentTask.getDueTime());
        holder.cardView.setCardBackgroundColor(getColor(currentTask.getStatus()));
    }

    private int getColor(String status) {
        switch (status.toLowerCase()) {
            case "done":
                return ContextCompat.getColor(context, R.color.done);
            case "todo":
                return ContextCompat.getColor(context, R.color.todo);
            case "doing":
                return ContextCompat.getColor(context, R.color.doing);
            default:
                return ContextCompat.getColor(context, R.color.hellgrau);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView taskName;
        TextView subjectName;
        TextView status;
        TextView creationDate;
        TextView dueDate;
        TextView dueTime;
        CardView cardView;

        public TaskViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            subjectName = itemView.findViewById(R.id.disp);
            status = itemView.findViewById(R.id.status);
            creationDate = itemView.findViewById(R.id.creationDate);
            dueDate = itemView.findViewById(R.id.dueDate);
            dueTime = itemView.findViewById(R.id.dueTime);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 0, 0, "Edit");
            menu.add(this.getAdapterPosition(), 1, 0, "Delete");
        }
    }
}
