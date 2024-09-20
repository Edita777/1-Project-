package com.example.twf_final.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Objects;

import at.wifi.swdev.noteapp.R;

public class MyDialog extends DialogFragment {

    public static final String TASK_ADD_DIALOG = "addTask";
    public static final String TASK_UPDATE_DIALOG = "updateTask";
    public static final String PARTICIPANT_ADD_DIALOG = "addParticipant";
    public static final String PARTICIPANT_UPDATE_DIALOG = "updateParticipant";

    private int roll;
    private String name;
    private String company;
    private String occupation;
    private String taskName;
    private String subjectName;
    private String taskStatus = "normal";
    private String creationDate;
    private String dueDate;
    private String dueTime;

    private OnClickListener listener;
    private OnTaskClickListener taskListener;
    private OnParticipantClickListener participantListener;

    private CheckBox todoCheckBox;
    private CheckBox doingCheckBox;
    private CheckBox doneCheckBox;

    public MyDialog() {}

    public MyDialog(int roll, String name, String company, String occupation) {
        this.roll = roll;
        this.name = name;
        this.company = company;
        this.occupation = occupation;
    }

    public MyDialog(String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime) {
        this.taskName = taskName;
        this.subjectName = subjectName;
        this.taskStatus = status;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public interface OnTaskClickListener {
        void onClick(String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime);
    }

    public interface OnClickListener {
        void onClick(String text1, String text2);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnParticipantClickListener {
        void onParticipantClick(String roll, String name, String company, String occupation);
    }

    public void setTaskListener(OnTaskClickListener listener) {
        this.taskListener = listener;
    }

    public void setParticipantListener(OnParticipantClickListener listener) {
        this.participantListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (Objects.equals(getTag(), TASK_ADD_DIALOG)) dialog = getAddTaskDialog();
        if (Objects.equals(getTag(), PARTICIPANT_ADD_DIALOG)) dialog = getAddParticipantDialog();
        if (Objects.equals(getTag(), TASK_UPDATE_DIALOG)) dialog = getUpdateTaskDialog();
        if (Objects.equals(getTag(), PARTICIPANT_UPDATE_DIALOG)) dialog = getUpdateParticipantDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return dialog;
    }

    private Dialog getAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Task");

        EditText taskEdt = view.findViewById(R.id.est01);
        EditText subjectEdt = view.findViewById(R.id.est02);
        EditText dueDateEdt = view.findViewById(R.id.dueDate);
        TimePicker dueTimePicker = view.findViewById(R.id.dueTime);

        taskEdt.setHint("Task Name");
        subjectEdt.setHint("Subject Name");
        dueDateEdt.setHint("Due Date (YYYY-MM-DD)");

        todoCheckBox = view.findViewById(R.id.todo);
        doingCheckBox = view.findViewById(R.id.doing);
        doneCheckBox = view.findViewById(R.id.done);

        setCheckBoxListeners();

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String taskName = taskEdt.getText().toString();
            String subjectName = subjectEdt.getText().toString();
            String dueDate = dueDateEdt.getText().toString();
            String dueTime = String.format("%02d:%02d", dueTimePicker.getHour(), dueTimePicker.getMinute());
            String creationDate = LocalDate.now().toString();

            if (taskListener != null) {
                taskListener.onClick(taskName, subjectName, taskStatus, creationDate, dueDate, dueTime);
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getUpdateTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Task");

        EditText taskEdt = view.findViewById(R.id.est01);
        EditText subjectEdt = view.findViewById(R.id.est02);
        EditText dueDateEdt = view.findViewById(R.id.dueDate);
        TimePicker dueTimePicker = view.findViewById(R.id.dueTime);

        taskEdt.setHint("Task Name");
        subjectEdt.setHint("Subject Name");
        dueDateEdt.setHint("Due Date (YYYY-MM-DD)");

        taskEdt.setText(taskName);
        subjectEdt.setText(subjectName);
        dueDateEdt.setText(dueDate);
        if (dueTime != null) {
            String[] timeParts = dueTime.split(":");
            dueTimePicker.setHour(Integer.parseInt(timeParts[0]));
            dueTimePicker.setMinute(Integer.parseInt(timeParts[1]));
        }

        todoCheckBox = view.findViewById(R.id.todo);
        doingCheckBox = view.findViewById(R.id.doing);
        doneCheckBox = view.findViewById(R.id.done);

        switch (taskStatus) {
            case "todo":
                todoCheckBox.setChecked(true);
                break;
            case "doing":
                doingCheckBox.setChecked(true);
                break;
            case "done":
                doneCheckBox.setChecked(true);
                break;
        }

        setCheckBoxListeners();

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String taskName = taskEdt.getText().toString();
            String subjectName = subjectEdt.getText().toString();
            String dueDate = dueDateEdt.getText().toString();
            String dueTime = String.format("%02d:%02d", dueTimePicker.getHour(), dueTimePicker.getMinute());
            String creationDate = LocalDate.now().toString();

            if (taskListener != null) {
                taskListener.onClick(taskName, subjectName, taskStatus, creationDate, dueDate, dueTime);
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getAddParticipantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_participant, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Participant");

        EditText rollEdt = view.findViewById(R.id.est01);
        EditText nameEdt = view.findViewById(R.id.est02);
        EditText companyEdt = view.findViewById(R.id.est03);
        EditText occupationEdt = view.findViewById(R.id.est04);

        rollEdt.setHint("Roll");
        nameEdt.setHint("Name");
        companyEdt.setHint("Company Name");
        occupationEdt.setHint("Occupation");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = rollEdt.getText().toString();
            String name = nameEdt.getText().toString();
            String company = companyEdt.getText().toString();
            String occupation = occupationEdt.getText().toString();

            if (participantListener != null) {
                participantListener.onParticipantClick(roll, name, company, occupation);
            } else {
                Toast.makeText(getContext(), "Listener is not set", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getUpdateParticipantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_participant, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Participant");

        EditText rollEdt = view.findViewById(R.id.est01);
        EditText nameEdt = view.findViewById(R.id.est02);
        EditText companyEdt = view.findViewById(R.id.est03);
        EditText occupationEdt = view.findViewById(R.id.est04);

        rollEdt.setHint("Roll");
        nameEdt.setHint("Name");
        companyEdt.setHint("Company Name");
        occupationEdt.setHint("Occupation");

        rollEdt.setText(String.valueOf(roll));
        rollEdt.setEnabled(false);
        nameEdt.setText(name);
        companyEdt.setText(company);
        occupationEdt.setText(occupation);

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button update = view.findViewById(R.id.add_btn);
        update.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        update.setOnClickListener(v -> {
            String roll = rollEdt.getText().toString();
            String name = nameEdt.getText().toString();
            String company = companyEdt.getText().toString();
            String occupation = occupationEdt.getText().toString();

            if (participantListener != null) {
                participantListener.onParticipantClick(roll, name, company, occupation);
            } else {
                Toast.makeText(getContext(), "Listener is not set", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        return builder.create();
    }

    private void setCheckBoxListeners() {
        todoCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskStatus = "todo";
                doingCheckBox.setChecked(false);
                doneCheckBox.setChecked(false);
            } else if (!doingCheckBox.isChecked() && !doneCheckBox.isChecked()) {
                taskStatus = "normal";
            }
        });

        doingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskStatus = "doing";
                todoCheckBox.setChecked(false);
                doneCheckBox.setChecked(false);
            } else if (!todoCheckBox.isChecked() && !doneCheckBox.isChecked()) {
                taskStatus = "normal";
            }
        });

        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                taskStatus = "done";
                todoCheckBox.setChecked(false);
                doingCheckBox.setChecked(false);
            } else if (!todoCheckBox.isChecked() && !doingCheckBox.isChecked()) {
                taskStatus = "normal";
            }
        });
    }
}
