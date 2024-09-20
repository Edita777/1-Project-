package com.example.twf_final.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.viewModel.TaskViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class TaskFragment extends Fragment {
    private Button fab;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TaskEntity> taskItems = new ArrayList<>();
    private Toolbar toolbar;
    private TaskViewModel taskViewModel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        fab = view.findViewById(R.id.button2);
        fab.setOnClickListener(v -> {
            showDialog();
//            setToolbarVisible();
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(getContext(), taskItems);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                gotoItemFragment(position);
            }

            @Override
            public void onEditClick(int position) {
                showUpdateDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteTask(position);
            }
        });

        taskViewModel.getGlobalTasks().observe(getViewLifecycleOwner(), globalTasks -> {
            taskItems.clear();
            taskItems.addAll(globalTasks);
            taskAdapter.notifyDataSetChanged();
        });

        taskViewModel.getProjectTasks().observe(getViewLifecycleOwner(), projectTasks -> {
            taskItems.addAll(projectTasks);
            taskAdapter.notifyDataSetChanged();
        });

        setToolbar(view);
        return view;
    }

    private void setToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.INVISIBLE);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subTitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        if (title != null) {
            title.setText("Todo List");
        }
        if (subTitle != null) {
            subTitle.setVisibility(View.GONE);
        }
        if (back != null) {
            back.setVisibility(View.INVISIBLE);
        }
        if (save != null) {
            save.setVisibility(View.INVISIBLE);
        }
    }


    private void gotoItemFragment(int position) {
        TaskEntity task = taskItems.get(position);
        ParticipantFragment participantFragment = ParticipantFragment.newInstance(task.getTaskName(), task.getSubjectName(), task.getId());

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, participantFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getParentFragmentManager(), MyDialog.TASK_ADD_DIALOG);
        dialog.setTaskListener((taskName, subjectName, status, creationDate, dueDate, dueTime) -> addTask(taskName, subjectName, status, creationDate, dueDate, dueTime));
    }

    private void addTask(String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime) {
        TaskEntity task = new TaskEntity(taskName, subjectName, status, creationDate, dueDate, dueTime, 0); // projectId = 0 for global task
        taskItems.add(task);
        sortTasksByNewestFirst(taskItems);
        taskAdapter.notifyDataSetChanged();
        taskViewModel.insert(task, false);
    }

    private void sortTasksByNewestFirst(List<TaskEntity> tasks) {
        Collections.sort(tasks, new Comparator<TaskEntity>() {
            @Override
            public int compare(TaskEntity task1, TaskEntity task2) {
                return Long.compare(task2.getCreationDateMillis(), task1.getCreationDateMillis()); // Compare by creation date, newest first
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                return true;
            case 1:
                deleteTask(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showUpdateDialog(int position) {
        TaskEntity task = taskItems.get(position);
        MyDialog dialog = new MyDialog(task.getTaskName(), task.getSubjectName(), task.getStatus(), task.getCreationDate(), task.getDueDate(), task.getDueTime());
        dialog.show(getParentFragmentManager(), MyDialog.TASK_UPDATE_DIALOG);
        dialog.setTaskListener((taskName, subjectName, status, creationDate, dueDate, dueTime) -> updateTask(position, taskName, subjectName, status, creationDate, dueDate, dueTime));
    }

    private void updateTask(int position, String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime) {
        TaskEntity taskEntity = taskItems.get(position);
        taskEntity.setTaskName(taskName);
        taskEntity.setSubjectName(subjectName);
        taskEntity.setStatus(status);
        taskEntity.setCreationDate(creationDate);
        taskEntity.setDueDate(dueDate);
        taskEntity.setDueTime(dueTime);
        taskViewModel.update(taskEntity);
        sortTasksByNewestFirst(taskItems);
        taskAdapter.notifyItemChanged(position);
    }

    private void deleteTask(int position) {
        TaskEntity taskEntity = taskItems.get(position);
        taskViewModel.delete(taskEntity);
        taskItems.remove(position);
        taskAdapter.notifyItemRemoved(position);
    }
}
