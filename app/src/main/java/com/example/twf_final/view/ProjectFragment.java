package com.example.twf_final.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.MainActivity;
import com.example.twf_final.dataBase.entity.ProjectEntity;
import com.example.twf_final.viewModel.ProjectViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class ProjectFragment extends Fragment {

    private Button fab;
    private RecyclerView recyclerView;
    private ProjectAdapter projectAdapter;
    private ProjectViewModel projectViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        fab = view.findViewById(R.id.button3);
        fab.setOnClickListener(v -> showDialog());

        recyclerView = view.findViewById(R.id.recyclerViewProject);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        projectAdapter = new ProjectAdapter(new ArrayList<>());
        recyclerView.setAdapter(projectAdapter);

        projectViewModel.getAllProjects().observe(getViewLifecycleOwner(), projects -> {
            if (projects != null) {
                List<ProjectEntity> reversedProjects = new ArrayList<>(projects);
                Collections.reverse(reversedProjects);
                projectAdapter.updateProjects(reversedProjects);
            }
        });

        projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                ProjectEntity projectEntity = projectAdapter.getProjectAt(position);
                if (projectEntity != null) {
                    gotoItemFragment(projectEntity, position);
                    projectEntity.setClicked(true);
                    projectViewModel.update(projectEntity);
                }
            }

            @Override
            public void onEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDelete(int position) {
                deleteProject(position);
            }
        });

        setToolbar();
    }

    private void showDialog() {
        DialogProject dialog = new DialogProject();
        dialog.show(requireActivity().getSupportFragmentManager(), DialogProject.PROJECT_ADD_DIALOG);
        dialog.setListener(this::addProject);
    }

    private void addProject(String projectNumber, String projectName, String description) {
        try {
            long projectNum = Long.parseLong(projectNumber);
            ProjectEntity projectEntity = new ProjectEntity(projectNum, projectName, description);
            projectViewModel.insert(projectEntity);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Project Number must be a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(int position) {
    }

    private void deleteProject(int position) {
        ProjectEntity projectEntity = projectAdapter.getProjectAt(position);
        if (projectEntity != null) {
            projectViewModel.delete(projectEntity);
        }
    }
    public void gotoItemFragment(ProjectEntity projectEntity, int position) {
        ItemFragment itemFragment = ItemFragment.newInstance(projectEntity.getId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, itemFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setToolbar() {
    }
}
