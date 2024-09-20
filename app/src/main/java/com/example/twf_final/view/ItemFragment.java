package com.example.twf_final.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.entity.DocumentEntity;
import com.example.twf_final.dataBase.entity.ParticipantEntity;
import com.example.twf_final.dataBase.entity.PictureEntity;
import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.viewModel.DocumentViewModel;
import com.example.twf_final.viewModel.ParticipantViewModel;
import com.example.twf_final.viewModel.TaskViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class ItemFragment extends DialogFragment {

    private static final int PICK_DOCUMENT_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    public static final int PERMISSION_REQUEST_CODE = 100;

    private AppDataBase appDatabase;
    private RecyclerView taskRecycle;
    private DocumentAdapter documentAdapter;
    private PictureAdapter pictureAdapter;
    private TaskAdapter taskAdapter;
    private ParticipantAdapter projectParticipantAdapter;
    private ParticipantAdapter taskParticipantAdapter;
    private ArrayList<TaskEntity> taskItems = new ArrayList<>();
    private ArrayList<ParticipantEntity> projectParticipantItems = new ArrayList<>();
    private ArrayList<ParticipantEntity> taskParticipantItems = new ArrayList<>();
    private List<File> documents = new ArrayList<>();
    private List<File> pictures = new ArrayList<>();
    private List<TaskEntity> tasks = new ArrayList<>();
    private DocumentViewModel documentViewModel;
    private List<ParticipantEntity> projectParticipants = new ArrayList<>();
    private List<ParticipantEntity> taskParticipants = new ArrayList<>();

    private String currentPhotoPath;
    private Spinner spinner;
    private Toolbar toolbar;
    private TaskViewModel taskViewModel;
    private ParticipantViewModel participantViewModel;
    private long projectId;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(long projectId) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putLong("project_id", projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        participantViewModel = new ViewModelProvider(requireActivity()).get(ParticipantViewModel.class);
        documentViewModel = new ViewModelProvider(requireActivity()).get(DocumentViewModel.class);
        if (getArguments() != null) {
            projectId = getArguments().getLong("project_id");
        }

        // Initialize Views
        Button taskButton = view.findViewById(R.id.taskButton);
        Button participantButton = view.findViewById(R.id.participantButton);
        Button documentButton = view.findViewById(R.id.documentButton);

        Button saveButton = view.findViewById(R.id.buttonSave);
        Button cancelButton = view.findViewById(R.id.button1);
        spinner = view.findViewById(R.id.spinner);
        taskRecycle = view.findViewById(R.id.taskRecycle);
        toolbar = view.findViewById(R.id.toolbar);

        taskButton.setOnClickListener(v -> showTaskDialog());
        participantButton.setOnClickListener(v -> showParticipantDialog());

        taskRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        documentAdapter = new DocumentAdapter(documents, new DocumentAdapter.OnDocumentActionListener() {
            @Override
            public void onDelete(File document) {
                deleteDocument(document);
            }

            @Override
            public void onUpdate(File document) {
                updateDocument(document);
            }
        });
        pictureAdapter = new PictureAdapter(pictures);
        taskAdapter = new TaskAdapter(getContext(), tasks);
        projectParticipantAdapter = new ParticipantAdapter(getContext(), projectParticipants);
        taskParticipantAdapter = new ParticipantAdapter(getContext(), taskParticipants);

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                long taskId = tasks.get(position).getId();
                setTaskId(taskId);
                gotoTaskFragment(position);
            }

            @Override
            public void onEditClick(int position) {
                showUpdateTaskDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteTask(position);
            }
        });

        projectParticipantAdapter.setOnItemClickListener(new ParticipantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                gotoParticipantFragment(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteProjectParticipant(position);
            }

            @Override
            public void onEditClick(int position) {
                showUpdateProjectParticipantDialog(position);
            }
        });

        taskParticipantAdapter.setOnItemClickListener(new ParticipantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeStatus(position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteTaskParticipant(position);
            }

            @Override
            public void onEditClick(int position) {
                showUpdateTaskParticipantDialog(position);
            }
        });

        taskRecycle.setAdapter(documentAdapter);

        appDatabase = AppDataBase.getInstance(requireContext());

        if (!hasPermissions()) {
            requestPermissions();
        }

        documentButton.setOnClickListener(v -> openDocumentPicker());
        saveButton.setOnClickListener(v -> {
            saveUpdates();
            navigateToProjectFragment();
        });
        cancelButton.setOnClickListener(v -> navigateToProjectFragment());

        setupSpinner();
        setToolbar(view);

        taskViewModel.getProjectTasks().observe(getViewLifecycleOwner(), projectTasks -> {
            Log.d("ItemFragment", "Project tasks updated: " + projectTasks.size());
            tasks.clear();
            for (TaskEntity task : projectTasks) {
                if (task.getProjectId() == projectId) {
                    tasks.add(task);
                }
            }
            taskAdapter.notifyDataSetChanged();
        });

        participantViewModel.getProjectAddedParticipants().observe(getViewLifecycleOwner(), addedParticipants -> {
            Log.d("ItemFragment", "Added participants updated: " + addedParticipants.size());
            projectParticipants.clear();
            projectParticipants.addAll(addedParticipants);
            projectParticipantAdapter.notifyDataSetChanged();
        });

        documentViewModel.getAllDocuments().observe(getViewLifecycleOwner(), addedDocuments -> {
            Log.d("ItemFragment", "Added documents updated: " + addedDocuments.size());
            documents.clear();
            for (DocumentEntity documentEntity : addedDocuments) {
                documents.add(new File(documentEntity.getFilePath()));
            }
            documentAdapter.notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_item);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    private void saveUpdates() {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            for (TaskEntity task : tasks) {
                taskViewModel.update(task);
            }
            for (File document : documents) {
                DocumentEntity documentEntity = new DocumentEntity(document.getAbsolutePath());
                appDatabase.getDocumentDao().insert(documentEntity);
            }
            for (File picture : pictures) {
                PictureEntity pictureEntity = new PictureEntity(picture.getAbsolutePath());
                appDatabase.getPictureDao().insert(pictureEntity);
            }
            for (ParticipantEntity participant : projectParticipants) {
                participantViewModel.update(participant);
            }
            for (ParticipantEntity participant : taskParticipants) {
                participantViewModel.update(participant);
            }
        });
    }

    private void navigateToProjectFragment() {
        Fragment projectFragment = new ProjectFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, projectFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(getContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showUpdateProjectParticipantDialog(int position) {
        if (position >= 0 && position < projectParticipants.size()) {
            ParticipantEntity participant = projectParticipants.get(position);
            MyDialog dialog = new MyDialog(participant.getRoll(), participant.getName(), participant.getCompany(), participant.getOccupation());
            dialog.show(getParentFragmentManager(), MyDialog.PARTICIPANT_UPDATE_DIALOG);
            dialog.setParticipantListener((roll, name, company, occupation) -> updateProjectParticipant(position, roll, name, company, occupation));
        } else {
            Log.e("ItemFragment", "Invalid participant position: " + position);
        }
    }


    private void showUpdateTaskParticipantDialog(int position) {
        ParticipantEntity participant = taskParticipants.get(position);
        MyDialog dialog = new MyDialog(participant.getRoll(), participant.getName(), participant.getCompany(), participant.getOccupation());
        dialog.show(getParentFragmentManager(), MyDialog.PARTICIPANT_UPDATE_DIALOG);
        dialog.setParticipantListener((roll, name, company, occupation) -> updateTaskParticipant(position, roll, name, company, occupation));
    }

    private void openDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_DOCUMENT_REQUEST);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error occurred while creating the file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.twf_final.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DOCUMENT_REQUEST && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = getPathFromUri(uri);
                if (!TextUtils.isEmpty(path)) {
                    File documentFile = new File(path);
                    insertDocumentIntoDatabase(documentFile);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            File pictureFile = new File(currentPhotoPath);
            insertPictureIntoDatabase(pictureFile);
        }
    }

    private String getPathFromUri(Uri uri) {
        return uri.getPath();
    }

    private void insertDocumentIntoDatabase(File documentFile) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            DocumentEntity documentEntity = new DocumentEntity(documentFile.getAbsolutePath());
            appDatabase.getDocumentDao().insert(documentEntity);
            getActivity().runOnUiThread(() -> {
                documents.add(documentFile);
                documentAdapter.notifyDataSetChanged();
            });
        });
    }

    private void insertPictureIntoDatabase(File pictureFile) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            PictureEntity pictureEntity = new PictureEntity(pictureFile.getAbsolutePath());
            appDatabase.getPictureDao().insert(pictureEntity);
            getActivity().runOnUiThread(() -> {
                pictures.add(pictureFile);
                pictureAdapter.notifyDataSetChanged();
            });
        });
    }

    private void deleteDocument(File documentFile) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            DocumentEntity documentEntity = new DocumentEntity(documentFile.getAbsolutePath());
            appDatabase.getDocumentDao().delete(documentEntity);
            getActivity().runOnUiThread(() -> {
                documents.remove(documentFile);
                documentAdapter.notifyDataSetChanged();
            });
        });
    }

    private void updateDocument(File documentFile) {
        Toast.makeText(getContext(), "Update document: " + documentFile.getName(), Toast.LENGTH_SHORT).show();
    }

    private void setupSpinner() {
        List<String> items = new ArrayList<>();
        items.add("Documents");
        items.add("Tasks");
        items.add("Project Participants");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    taskRecycle.setAdapter(documentAdapter);
                }  else if (position == 1) {
                    taskRecycle.setAdapter(taskAdapter);
                } else if (position == 2) {
                    taskRecycle.setAdapter(projectParticipantAdapter);
                } else if (position == 3) {
                    taskRecycle.setAdapter(taskParticipantAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setTaskId(long taskId) {
        participantViewModel.getTaskParticipants(taskId).observe(getViewLifecycleOwner(), addedParticipants -> {
            Log.d("ItemFragment", "Task participants updated: " + addedParticipants.size());
            taskParticipants.clear();
            taskParticipants.addAll(addedParticipants);
            taskParticipantAdapter.notifyDataSetChanged();
        });
    }

    private void showTaskDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getParentFragmentManager(), MyDialog.TASK_ADD_DIALOG);
        dialog.setTaskListener((taskName, subjectName, status, creationDate, dueDate, dueTime) -> addTask(taskName, subjectName, status, creationDate, dueDate, dueTime));
    }

    private void showParticipantDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getParentFragmentManager(), MyDialog.PARTICIPANT_ADD_DIALOG);
        dialog.setParticipantListener((roll, name, company, occupation) -> addProjectParticipant(roll, name, company, occupation));
    }

    private void addTask(String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime) {
        TaskEntity task = new TaskEntity(taskName, subjectName, status, creationDate, dueDate, dueTime, projectId);
        taskViewModel.insert(task, true);
        taskAdapter.notifyDataSetChanged();
    }

    private void addProjectParticipant(String rollString, String name, String company, String occupation) {
        int roll = Integer.parseInt(rollString);
        ParticipantEntity participant = new ParticipantEntity(roll, name, company, occupation, projectId);
        participantViewModel.insertProjectParticipant(participant);
    }

    private void updateProjectParticipant(int position, String rollString, String name, String company, String occupation) {
        if (position >= 0 && position < projectParticipants.size()) {
            ParticipantEntity participant = projectParticipants.get(position);
            participant.setRoll(Integer.parseInt(rollString));
            participant.setName(name);
            participant.setCompany(company);
            participant.setOccupation(occupation);
            participantViewModel.update(participant);
            // Ensure this code runs on the UI thread
            requireActivity().runOnUiThread(() -> projectParticipantAdapter.notifyItemChanged(position));
        } else {
            Log.e("ItemFragment", "Invalid participant position: " + position);
        }
    }

    private void deleteProjectParticipant(int position) {
        Log.d("ItemFragment", "Attempting to delete participant at position: " + position);
        Log.d("ItemFragment", "Project participants size: " + projectParticipants.size());
        if (position >= 0 && position < projectParticipants.size()) {
            ParticipantEntity participant = projectParticipants.get(position);
            Log.d("ItemFragment", "Deleting participant: " + participant.getName());
            participantViewModel.deleteProjectParticipant(participant);
            // Ensure this code runs on the UI thread
            requireActivity().runOnUiThread(() -> {
                if (position < projectParticipants.size()) {
                    projectParticipants.remove(position);
                    projectParticipantAdapter.notifyItemRemoved(position);
                }
            });
        } else {
            Log.e("ItemFragment", "Invalid participant position: " + position);
        }
    }

    private void updateTaskParticipant(int position, String rollString, String name, String company, String occupation) {
        if (position >= 0 && position < taskParticipants.size()) {
            ParticipantEntity participant = taskParticipants.get(position);
            participant.setRoll(Integer.parseInt(rollString));
            participant.setName(name);
            participant.setCompany(company);
            participant.setOccupation(occupation);
            participantViewModel.update(participant);
            taskParticipantAdapter.notifyItemChanged(position);
        } else {
            Log.e("ItemFragment", "Invalid task participant position: " + position);
        }
    }

    private void deleteTaskParticipant(int position) {
        if (position >= 0 && position < taskParticipants.size()) {
            ParticipantEntity participant = taskParticipants.get(position);
            participantViewModel.deleteTaskParticipant(participant);
            taskParticipants.remove(position);
            taskParticipantAdapter.notifyItemRemoved(position);
        } else {
            Log.e("ItemFragment", "Invalid task participant position: " + position);
        }
    }

    private void showUpdateTaskDialog(int position) {
        if (position >= 0 && position < tasks.size()) {
            TaskEntity task = tasks.get(position);
            MyDialog dialog = new MyDialog(task.getTaskName(), task.getSubjectName(), task.getStatus(), task.getCreationDate(), task.getDueDate(), task.getDueTime());
            dialog.show(getParentFragmentManager(), MyDialog.TASK_UPDATE_DIALOG);
            dialog.setTaskListener((taskName, subjectName, status, creationDate, dueDate, dueTime) -> updateTask(position, taskName, subjectName, status, creationDate, dueDate, dueTime));
        } else {
            Log.e("ItemFragment", "Invalid task position: " + position);
        }
    }

    private void updateTask(int position, String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime) {
        if (position >= 0 && position < tasks.size()) {
            TaskEntity taskEntity = tasks.get(position);
            taskEntity.setTaskName(taskName);
            taskEntity.setSubjectName(subjectName);
            taskEntity.setStatus(status);
            taskEntity.setDueDate(dueDate);
            taskEntity.setDueTime(dueTime);
            taskViewModel.update(taskEntity);
            taskAdapter.notifyItemChanged(position);
        } else {
            Log.e("ItemFragment", "Invalid task position: " + position);
        }
    }

    private void deleteTask(int position) {
        if (position >= 0 && position < tasks.size()) {
            TaskEntity taskEntity = tasks.get(position);
            taskViewModel.delete(taskEntity);
            tasks.remove(position);
            taskAdapter.notifyItemRemoved(position);
        } else {
            Log.e("ItemFragment", "Invalid task position: " + position);
        }
    }

    private void gotoTaskFragment(int position) {
        if (position >= 0 && position < tasks.size()) {
            TaskEntity task = tasks.get(position);
            ParticipantFragment participantFragment = ParticipantFragment.newInstance(task.getTaskName(), task.getSubjectName(), task.getId());

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, participantFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.e("ItemFragment", "Invalid task position: " + position);
        }
    }

    private void gotoParticipantFragment(int position) {
        if (position >= 0 && position < projectParticipants.size()) {
            ParticipantEntity participant = projectParticipants.get(position);
            // Handle participant click event here if needed
        } else {
            Log.e("ItemFragment", "Invalid participant position: " + position);
        }
    }

    private void setToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
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
    }


    private void setToolbarVisible() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }
        AppBarLayout appBarLayout = getView().findViewById(R.id.appBarLayout);
        if (appBarLayout != null) {
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        Log.d("ItemFragment", "Context menu item selected at position: " + position);
        switch (item.getItemId()) {
            case 0: // Edit
                if (position >= 0) {
                    if (spinner.getSelectedItemPosition() == 2) {
                        showUpdateProjectParticipantDialog(position);
                    } else if (spinner.getSelectedItemPosition() == 3) {
                        showUpdateTaskParticipantDialog(position);
                    }
                }
                return true;
            case 1: // Delete
                if (position >= 0) {
                    if (spinner.getSelectedItemPosition() == 2) {
                        deleteProjectParticipant(position);
                    } else if (spinner.getSelectedItemPosition() == 3) {
                        deleteTaskParticipant(position);
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void changeStatus(int position) {
        if (position >= 0 && position < taskParticipants.size()) {
            ParticipantEntity participant = taskParticipants.get(position);
            String currentStatus = participant.getStatus();
            String newStatus = currentStatus.equals("P") ? "A" : "P";
            participant.setStatus(newStatus);
            taskParticipantAdapter.notifyItemChanged(position);
        } else {
            Log.e("ItemFragment", "Invalid task participant position: " + position);
        }
    }
}
