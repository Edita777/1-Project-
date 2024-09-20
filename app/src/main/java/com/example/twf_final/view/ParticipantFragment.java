package com.example.twf_final.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.entity.ParticipantEntity;
import com.example.twf_final.dataBase.entity.StatusEntity;
import com.example.twf_final.viewModel.ParticipantViewModel;
import com.example.twf_final.viewModel.StatusViewModel;

import java.util.ArrayList;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class ParticipantFragment extends Fragment {
    private static final String ARG_TASK_NAME = "taskName";
    private static final String ARG_SUBJECT_NAME = "subjectName";
    private static final String ARG_CID = "cid";

    private String taskName;
    private String subjectName;
    private long cid;

    private RecyclerView recyclerView;
    private ParticipantAdapter participantAdapter;
    private ArrayList<ParticipantEntity> participantItems = new ArrayList<>();
    private ParticipantViewModel participantViewModel;
    private StatusViewModel statusViewModel;
    private MyCalendar calendar;
    private RecyclerView.LayoutManager layoutManager;
    private TextView subTitle;
    private Toolbar toolbar;

    public static ParticipantFragment newInstance(String taskName, String subjectName, long cid) {
        ParticipantFragment fragment = new ParticipantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_NAME, taskName);
        args.putString(ARG_SUBJECT_NAME, subjectName);
        args.putLong(ARG_CID, cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.participant_fragment, container, false);

        if (getArguments() != null) {
            taskName = getArguments().getString(ARG_TASK_NAME);
            subjectName = getArguments().getString(ARG_SUBJECT_NAME);
            cid = getArguments().getLong(ARG_CID);
        }

        calendar = new MyCalendar();
        participantViewModel = new ViewModelProvider(this).get(ParticipantViewModel.class);
        statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);

        setToolbar(view);

        recyclerView = view.findViewById(R.id.participant_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        participantAdapter = new ParticipantAdapter(getContext(), participantItems);
        recyclerView.setAdapter(participantAdapter);
        participantAdapter.setOnItemClickListener(new ParticipantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeStatus(position);
            }

            public void onEditClick(int position) {
                showUpdateParticipantDialog(position);
            }
            @Override
            public void onDeleteClick(int position) {
                deleteParticipant(position);
            }
        });

        participantViewModel.getTaskParticipants(cid).observe(getViewLifecycleOwner(), participants -> {
            participantItems.clear();
            participantItems.addAll(participants);
            participantAdapter.notifyDataSetChanged();
            loadStatusData();
        });

        return view;
    }

    private void setToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subTitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        save.setOnClickListener(v -> saveStatus()); // Save button click listener

        title.setText(taskName);
        subTitle.setText(subjectName + " | " + calendar.getDate());

        back.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        toolbar.inflateMenu(R.menu.dopdown_menu);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_participant) {
            showAddParticipantDialog();
        } else if (menuItem.getItemId() == R.id.show_Calender) {
            showCalendar();
        }
        return true;
    }

    private void showCalendar() {
        calendar.show(getParentFragmentManager(), "");
        calendar.setOnCalendarClickListener(this::onCalendarClicked);
    }

    private void onCalendarClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subTitle.setText(subjectName + " | " + calendar.getDate());
        loadStatusData();
    }

    private void showAddParticipantDialog() {
        MyDialog dialog = new MyDialog();
        dialog.setParticipantListener((roll, name, company, occupation) -> {
            try {
                int rollNumber = Integer.parseInt(roll.trim());
                addParticipant(rollNumber, name, company, occupation);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid roll number", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getParentFragmentManager(), MyDialog.PARTICIPANT_ADD_DIALOG);
    }

    private void addParticipant(int roll, String name, String company, String occupation) {
        ParticipantEntity participant = new ParticipantEntity(roll, name, company, occupation, cid);
        participantViewModel.insertTaskParticipant(participant);
    }

    private void saveStatus() {
        // Save the current status of each participant in the ViewModel
        for (ParticipantEntity participantItem : participantItems) {
            StatusEntity statusEntity = new StatusEntity(participantItem.getId(), participantItem.getTaskId(), calendar.getDate(), participantItem.getStatus());
            statusViewModel.insertStatus(statusEntity);
        }
        Toast.makeText(getContext(), "Status saved", Toast.LENGTH_SHORT).show();
    }

    private void loadStatusData() {
        for (ParticipantEntity participantItem : participantItems) {
            statusViewModel.getStatusForParticipant(participantItem.getId(), calendar.getDate()).observe(getViewLifecycleOwner(), status -> {
                if (status != null) {
                    participantItem.setStatus(status);
                } else {
                    participantItem.setStatus("");
                }
                participantAdapter.notifyDataSetChanged();
            });
        }
    }

    private void changeStatus(int position) {
        String status = participantItems.get(position).getStatus();
        if ("P".equals(status)) status = "A";
        else status = "P";

        participantItems.get(position).setStatus(status);
        participantAdapter.notifyItemChanged(position);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateParticipantDialog(item.getGroupId());
                return true;
            case 1:
                deleteParticipant(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showUpdateParticipantDialog(int position) {
        ParticipantEntity participant = participantItems.get(position);
        MyDialog dialog = new MyDialog(participant.getRoll(), participant.getName(), participant.getCompany(), participant.getOccupation());
        dialog.setParticipantListener((roll, name, company, occupation) -> updateParticipant(position, roll, name, company, occupation));
        dialog.show(getParentFragmentManager(), MyDialog.PARTICIPANT_UPDATE_DIALOG);
    }

    private void updateParticipant(int position, String roll, String name, String company, String occupation) {
        ParticipantEntity participantItem = participantItems.get(position);
        participantItem.setRoll(Integer.parseInt(roll));
        participantItem.setName(name);
        participantItem.setCompany(company);
        participantItem.setOccupation(occupation);
        participantViewModel.update(participantItem);
        participantAdapter.notifyItemChanged(position);
    }

    private void deleteParticipant(int position) {
        ParticipantEntity participantItem = participantItems.get(position);
        participantViewModel.deleteTaskParticipant(participantItem);
        participantItems.remove(position);
        participantAdapter.notifyItemRemoved(position);
    }
}
