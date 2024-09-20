package com.example.twf_final.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.wifi.swdev.noteapp.R;

public class ReminderDialogFragment extends DialogFragment {

    private static final String ARG_TASK_NAME = "task_name";
    private String taskName;

    public static ReminderDialogFragment newInstance(String taskName) {
        ReminderDialogFragment fragment = new ReminderDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_NAME, taskName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            taskName = getArguments().getString(ARG_TASK_NAME);
        }

        TextView textViewTaskName = view.findViewById(R.id.textViewTaskName);
        textViewTaskName.setText(taskName);

        Button buttonOk = view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(v -> dismiss());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String taskName = getArguments().getString(ARG_TASK_NAME);
        return new AlertDialog.Builder(requireContext())
                .setTitle("Task Reminder")
                .setMessage("The task \"" + taskName + "\" is due within 24 hours.")
                .setPositiveButton("OK", (dialog, which) -> dismiss())
                .create();
    }
}
