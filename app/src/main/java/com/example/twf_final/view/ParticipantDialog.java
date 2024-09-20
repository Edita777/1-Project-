package com.example.twf_final.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import at.wifi.swdev.noteapp.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ParticipantDialog extends DialogFragment {

    public static final String PARTICIPANT_ADD_DIALOG = "addParticipant";
    public static final String PARTICIPANT_UPDATE_DIALOG = "updateParticipant";

    private String roll;
    private String name;
    private String company;
    private String occupation;

    private OnParticipantClickListener participantListener;

    public ParticipantDialog(String roll, String name, String company, String occupation) {
        this.roll = roll;
        this.name = name;
        this.company = company;
        this.occupation = occupation;
    }

    public ParticipantDialog() {}

    public interface OnParticipantClickListener {
        void onParticipantClick(String roll, String name, String company, String occupation);
    }

    public void setParticipantListener(OnParticipantClickListener listener) {
        this.participantListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(PARTICIPANT_ADD_DIALOG)) dialog = getAddParticipantDialog();
        if (getTag().equals(PARTICIPANT_UPDATE_DIALOG)) dialog = getUpdateParticipantDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getAddParticipantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_participant, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Participant");

        EditText rollEdt = view.findViewById(R.id.est01);
        EditText nameEdt = view.findViewById(R.id.est02);
        EditText nameComp = view.findViewById(R.id.est03);
        EditText nameOccu = view.findViewById(R.id.est04);

        rollEdt.setHint("Roll");
        nameEdt.setHint("Name");
        nameComp.setHint("Company Name");
        nameOccu.setHint("Occupation");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll = rollEdt.getText().toString();
            String name = nameEdt.getText().toString();
            String company = nameComp.getText().toString();
            String occupation = nameOccu.getText().toString();
            participantListener.onParticipantClick(roll, name, company, occupation);
            dismiss();
        });

        return builder.create();
    }

    private Dialog getUpdateParticipantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_participant, null);
        builder.setView(view);
        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Participant");

        EditText rollEdt = view.findViewById(R.id.est01);
        EditText nameEdt = view.findViewById(R.id.est02);
        EditText nameComp = view.findViewById(R.id.est03);
        EditText nameOccu = view.findViewById(R.id.est04);

        rollEdt.setHint("Roll");
        nameEdt.setHint("Name");
        nameComp.setHint("Company Name");
        nameOccu.setHint("Occupation");
        rollEdt.setText(roll);
        nameEdt.setText(name);
        nameComp.setText(company);
        nameOccu.setText(occupation);

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button update = view.findViewById(R.id.add_btn);
        update.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        update.setOnClickListener(v -> {
            String roll = rollEdt.getText().toString();
            String name = nameEdt.getText().toString();
            String company = nameComp.getText().toString();
            String occupation = nameOccu.getText().toString();
            participantListener.onParticipantClick(roll, name, company, occupation);
            dismiss();
        });

        return builder.create();
    }
}

