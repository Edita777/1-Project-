package com.example.twf_final.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.entity.ProjectEntity;
import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.viewModel.ProjectViewModel;
import com.example.twf_final.viewModel.TaskViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class NotificationFragment extends Fragment {

    private RecyclerView projectRecyclerView;
    private RecyclerView taskRecyclerView;
    private RecyclerView dueDateRecyclerView;
    private ProjectAdapter projectAdapter;
    private TaskAdapter taskAdapter;
    private TaskAdapter dueDateAdapter;
    private ProjectViewModel projectViewModel;
    private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        projectRecyclerView = view.findViewById(R.id.recyclerViewNotifications);
        projectRecyclerView.setHasFixedSize(true);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        projectAdapter = new ProjectAdapter(new ArrayList<>());
        projectRecyclerView.setAdapter(projectAdapter);

        taskRecyclerView = view.findViewById(R.id.recyclerViewTasks);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>());
        taskRecyclerView.setAdapter(taskAdapter);

        dueDateRecyclerView = view.findViewById(R.id.recyclerViewDueDate);
        dueDateRecyclerView.setHasFixedSize(true);
        dueDateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dueDateAdapter = new TaskAdapter(getContext(), new ArrayList<>());
        dueDateRecyclerView.setAdapter(dueDateAdapter);

        observeViewModels();
    }

    private void observeViewModels() {
        projectViewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null) {
                List<ProjectEntity> reversedNotifications = new ArrayList<>(notifications);
                Collections.reverse(reversedNotifications);
                projectAdapter.updateProjects(reversedNotifications);
            }
        });

        taskViewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null) {
                List<TaskEntity> reversedNotifications = new ArrayList<>(notifications);
                Collections.reverse(reversedNotifications);
                taskAdapter.updateTasks(reversedNotifications);
            }
        });

        taskViewModel.getDueDateNotifications().observe(getViewLifecycleOwner(), dueDateNotifications -> {
            if (dueDateNotifications != null) {
                List<TaskEntity> reversedDueDateNotifications = new ArrayList<>(dueDateNotifications);
                Collections.reverse(reversedDueDateNotifications);
                dueDateAdapter.updateTasks(reversedDueDateNotifications);
            }
        });

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                ProjectEntity projectEntity = projectAdapter.getProjectAt(position);
                if (projectEntity != null) {
                    showProjectDialog(projectEntity);
                    projectEntity.setClicked(true);
                    projectViewModel.update(projectEntity);
                    removeProjectNotification(position);
                }
            }

            @Override
            public void onEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDelete(int position) {
                deleteProjectNotification(position);
            }
        });

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TaskEntity taskEntity = taskAdapter.getTaskAt(position);
                if (taskEntity != null) {
                    gotoTaskItemFragment(taskEntity, position);
                    taskEntity.setClicked(true);
                    taskViewModel.update(taskEntity);
                    removeTaskNotification(position);
                }
            }

            @Override
            public void onEditClick(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteTaskNotification(position);
            }
        });

        dueDateAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TaskEntity taskEntity = dueDateAdapter.getTaskAt(position);
                if (taskEntity != null) {
                    gotoTaskItemFragment(taskEntity, position);
                    taskEntity.setClicked(true);
                    taskViewModel.update(taskEntity);
                    removeDueDateNotification(position);
                }
            }

            @Override
            public void onEditClick(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteTaskNotification(position);
            }
        });
    }

    private void showEditDialog(int position) {
        // Implementation for showing edit dialog
    }

    private void deleteProjectNotification(int position) {
        ProjectEntity projectEntity = projectAdapter.getProjectAt(position);
        if (projectEntity != null) {
            projectViewModel.delete(projectEntity);
        }
    }

    private void deleteTaskNotification(int position) {
        TaskEntity taskEntity = taskAdapter.getTaskAt(position);
        if (taskEntity != null) {
            taskViewModel.delete(taskEntity);
        }
    }

    private void removeProjectNotification(int position) {
        projectAdapter.removeProjectAt(position);
        projectAdapter.notifyItemRemoved(position);
    }

    private void removeTaskNotification(int position) {
        taskAdapter.removeTaskAt(position);
        taskAdapter.notifyItemRemoved(position);
    }

    private void removeDueDateNotification(int position) {
        TaskEntity taskEntity = dueDateAdapter.getTaskAt(position);
        dueDateAdapter.removeTaskAt(position);
        dueDateAdapter.notifyItemRemoved(position);
        taskViewModel.removeDueDateNotification(taskEntity);
    }

    private void showProjectDialog(ProjectEntity projectEntity) {
        DialogProject dialog = DialogProject.newInstance(projectEntity.getProjectNumber(), projectEntity.getProjectName(), projectEntity.getDescription());
        dialog.show(requireActivity().getSupportFragmentManager(), DialogProject.PROJECT_ADD_DIALOG);
    }

    private void gotoTaskItemFragment(TaskEntity taskEntity, int position) {
        ParticipantFragment participantFragment = ParticipantFragment.newInstance(taskEntity.getTaskName(), taskEntity.getSubjectName(), taskEntity.getId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, participantFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void clearProjectAndTaskNotifications() {
        projectAdapter.updateProjects(new ArrayList<>());
        taskAdapter.updateTasks(new ArrayList<>());
        List<TaskEntity> dueDateTasks = taskViewModel.getDueDateNotifications().getValue();
        if (dueDateTasks != null) {
            dueDateAdapter.updateTasks(dueDateTasks);
        }
    }
}
