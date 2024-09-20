package com.example.twf_final.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import at.wifi.swdev.noteapp.R;
import com.example.twf_final.dataBase.entity.ProjectEntity;
import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.dataBase.entity.ParticipantEntity;
import com.example.twf_final.viewModel.ProjectViewModel;
import com.example.twf_final.viewModel.TaskViewModel;
import com.example.twf_final.viewModel.ParticipantViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private TextInputEditText searchEditText;
    private Spinner searchSpinner;
    private RecyclerView recyclerView;
    private ProjectAdapter projectAdapter;
    private TaskAdapter taskAdapter;
    private ParticipantAdapter participantAdapter;
    private ProjectViewModel projectViewModel;
    private TaskViewModel taskViewModel;
    private ParticipantViewModel participantViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        searchEditText = view.findViewById(R.id.tvSearch);
        searchSpinner = view.findViewById(R.id.search_bar);
        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        projectAdapter = new ProjectAdapter(new ArrayList<>());
        taskAdapter = new TaskAdapter(requireContext(), new ArrayList<>());
        participantAdapter = new ParticipantAdapter(requireContext(), new ArrayList<>());

        projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        participantViewModel = new ViewModelProvider(this).get(ParticipantViewModel.class);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                ProjectEntity projectEntity = projectAdapter.getProjectAt(position);
                if (projectEntity != null) {
                    gotoProjectItemFragment(projectEntity, position);
                }
            }

            @Override
            public void onEdit(int position) {
                // Handle edit project
            }

            @Override
            public void onDelete(int position) {
                // Handle delete project
            }
        });

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TaskEntity taskEntity = taskAdapter.getTaskAt(position);
                if (taskEntity != null) {
                    gotoTaskItemFragment(taskEntity, position);
                }
            }

            @Override
            public void onEditClick(int position) {
                // Handle edit task
            }

            @Override
            public void onDeleteClick(int position) {
                // Handle delete task
            }
        });

        return view;
    }

    private void performSearch(String query) {
        String selectedCategory = searchSpinner.getSelectedItem().toString();
        switch (selectedCategory) {
            case "Project":
                projectViewModel.getAllProjects().observe(getViewLifecycleOwner(), new Observer<List<ProjectEntity>>() {
                    @Override
                    public void onChanged(List<ProjectEntity> projects) {
                        List<ProjectEntity> filteredProjects = new ArrayList<>();
                        for (ProjectEntity project : projects) {
                            if (project.getProjectName().toLowerCase().contains(query.toLowerCase())) {
                                filteredProjects.add(project);
                            }
                        }
                        projectAdapter.updateProjects(filteredProjects);
                        recyclerView.setAdapter(projectAdapter);
                    }
                });
                break;
            case "Task":
                taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<TaskEntity>>() {
                    @Override
                    public void onChanged(List<TaskEntity> tasks) {
                        List<TaskEntity> filteredTasks = new ArrayList<>();
                        for (TaskEntity task : tasks) {
                            if (task.getTaskName().toLowerCase().contains(query.toLowerCase())) {
                                filteredTasks.add(task);
                            }
                        }
                        taskAdapter.updateTasks(filteredTasks);
                        recyclerView.setAdapter(taskAdapter);
                    }
                });
                break;
            case "Participant":
                participantViewModel.getAllParticipants().observe(getViewLifecycleOwner(), new Observer<List<ParticipantEntity>>() {
                    @Override
                    public void onChanged(List<ParticipantEntity> participants) {
                        List<ParticipantEntity> filteredParticipants = new ArrayList<>();
                        for (ParticipantEntity participant : participants) {
                            if (participant.getName().toLowerCase().contains(query.toLowerCase())) {
                                filteredParticipants.add(participant);
                            }
                        }
                        participantAdapter.updateParticipants(filteredParticipants);
                        recyclerView.setAdapter(participantAdapter);
                    }
                });
                break;
        }
    }

    private void gotoProjectItemFragment(ProjectEntity projectEntity, int position) {
        ItemFragment itemFragment = ItemFragment.newInstance(projectEntity.getId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, itemFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void gotoTaskItemFragment(TaskEntity taskEntity, int position) {
        ParticipantFragment participantFragment = ParticipantFragment.newInstance(taskEntity.getTaskName(), taskEntity.getSubjectName(), taskEntity.getId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, participantFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
