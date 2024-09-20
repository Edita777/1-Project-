package com.example.twf_final.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import at.wifi.swdev.noteapp.R;

public class DialogProject extends DialogFragment {

    public static final String PROJECT_ADD_DIALOG = "addProject";
    public static final String PROJECT_UPDATE_DIALOG = "updateProject";

    private String projectNumber;
    private String projectName;
    private String description;
    private OnClickListener listener;

    public DialogProject() {
    }

    public DialogProject(String projectNumber, String projectName, String description) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.description = description;
    }

    public interface OnClickListener {
        void onClick(String text1, String text2, String text3);
    }

    public void setListener(DialogProject.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(PROJECT_ADD_DIALOG)) dialog = getAddProjectDialog();
        if (getTag().equals(PROJECT_UPDATE_DIALOG)) dialog = getUpdateProjectDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return dialog;
    }
    public static DialogProject newInstance(long projectNumber, String projectName, String description) {
        DialogProject dialog = new DialogProject();
        Bundle args = new Bundle();
        args.putLong("projectNumber", projectNumber);
        args.putString("projectName", projectName);
        args.putString("description", description);
        dialog.setArguments(args);
        return dialog;
    }

    private Dialog getUpdateProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_project, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleProject);
        title.setText("Update Project");

        EditText editTextProjectNumber = view.findViewById(R.id.projectNumber);
        EditText editTextProjectName = view.findViewById(R.id.projectName);
        EditText editTextDescription = view.findViewById(R.id.description);

        editTextProjectNumber.setHint("Project Number");
        editTextProjectName.setHint("Project Name");
        editTextDescription.setHint("Description");

        editTextProjectNumber.setText(projectNumber);
        editTextProjectName.setText(projectName);
        editTextDescription.setText(description);

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button update = view.findViewById(R.id.add_btn);
        update.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        update.setOnClickListener(v -> {
            String updatedProjectNumber = editTextProjectNumber.getText().toString();
            String updatedProjectName = editTextProjectName.getText().toString();
            String updatedDescription = editTextDescription.getText().toString();

            listener.onClick(updatedProjectNumber, updatedProjectName, updatedDescription);
            dismiss();
        });

        return builder.create();
    }

    private Dialog getAddProjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_project, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleProject);
        title.setText("Add New Project");

        EditText editTextProjectNumber = view.findViewById(R.id.projectNumber);
        EditText editTextProjectName = view.findViewById(R.id.projectName);
        EditText editTextDescription = view.findViewById(R.id.description);

        editTextProjectNumber.setHint("Project Number");
        editTextProjectName.setHint("Project Name");
        editTextDescription.setHint("Description");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String newProjectNumber = editTextProjectNumber.getText().toString();
            String newProjectName = editTextProjectName.getText().toString();
            String newDescription = editTextDescription.getText().toString();

            listener.onClick(newProjectNumber, newProjectName, newDescription);
            dismiss();
        });

        return builder.create();
    }
}
