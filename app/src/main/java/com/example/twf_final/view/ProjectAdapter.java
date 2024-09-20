package com.example.twf_final.view;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.entity.ProjectEntity;

import java.util.ArrayList;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<ProjectEntity> projectEntities;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
        void onEdit(int position);
        void onDelete(int position);
    }

    public ProjectAdapter(List<ProjectEntity> projectEntities) {
        this.projectEntities = projectEntities;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateProjects(List<ProjectEntity> projectEntities) {
        this.projectEntities = projectEntities != null ? projectEntities : new ArrayList<>();
        notifyDataSetChanged();
    }

    public ProjectEntity getProjectAt(int position) {
        if (projectEntities != null && position >= 0 && position < projectEntities.size()) {
            return projectEntities.get(position);
        }
        return null;
    }

    public void removeProjectAt(int position) {
        projectEntities.remove(position);
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView projectNumber;
        TextView projectName;
        TextView description;
        OnItemClickListener onItemClickListener;
        CardView cardView;

        ProjectViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            projectNumber = itemView.findViewById(R.id.projectNumber);
            projectName = itemView.findViewById(R.id.projectName);
            description = itemView.findViewById(R.id.description);
            cardView = itemView.findViewById(R.id.projectCardView);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null && position < projectEntities.size()) {
                    onItemClickListener.onClick(position);
                    ProjectEntity projectEntity = projectEntities.get(position);
                    projectEntity.setClicked(true);
                    notifyItemChanged(position);
                }
            });
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(this.getAdapterPosition(), 0, 0, "EDIT");
            MenuItem delete = menu.add(this.getAdapterPosition(), 1, 0, "DELETE");
            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListener != null && position < projectEntities.size()) {
                switch (item.getItemId()) {
                    case 0:
                        onItemClickListener.onEdit(position);
                        return true;
                    case 1:
                        onItemClickListener.onDelete(position);
                        return true;
                }
            }
            return false;
        }
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ProjectViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        if (projectEntities != null && position < projectEntities.size()) {
            ProjectEntity projectEntity = projectEntities.get(position);
            holder.projectNumber.setText(String.valueOf(projectEntity.getProjectNumber()));
            holder.projectName.setText(projectEntity.getProjectName());
            holder.description.setText(projectEntity.getDescription());

            if (projectEntity.isClicked()) {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.hellgrau));
            } else {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lightgrey));
            }
        }
    }

    @Override
    public int getItemCount() {
        return projectEntities != null ? projectEntities.size() : 0;
    }
}
