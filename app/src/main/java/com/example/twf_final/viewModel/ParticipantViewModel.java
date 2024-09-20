package com.example.twf_final.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twf_final.dataBase.entity.ParticipantEntity;
import com.example.twf_final.repository.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

public class ParticipantViewModel extends AndroidViewModel {

    private ParticipantRepository repository;
    private LiveData<List<ParticipantEntity>> allParticipants;
    private MutableLiveData<List<ParticipantEntity>> projectParticipants = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<ParticipantEntity>> taskParticipants = new MutableLiveData<>(new ArrayList<>());

    public ParticipantViewModel(@NonNull Application application) {
        super(application);
        repository = new ParticipantRepository(application);
        allParticipants = repository.getAllParticipants();
    }

    public void insertProjectParticipant(ParticipantEntity participant) {
        repository.insert(participant);
        List<ParticipantEntity> currentParticipants = projectParticipants.getValue();
        currentParticipants.add(participant);
        projectParticipants.setValue(currentParticipants);
    }

    public void insertTaskParticipant(ParticipantEntity participant) {
        repository.insert(participant);
        List<ParticipantEntity> currentParticipants = taskParticipants.getValue();
        currentParticipants.add(participant);
        taskParticipants.setValue(currentParticipants);
    }

    public void update(ParticipantEntity participant) {
        repository.update(participant);
    }

    public void deleteProjectParticipant(ParticipantEntity participant) {
        repository.delete(participant);
        List<ParticipantEntity> currentParticipants = projectParticipants.getValue();
        currentParticipants.remove(participant);
        projectParticipants.setValue(currentParticipants);
    }

    public void deleteTaskParticipant(ParticipantEntity participant) {
        repository.delete(participant);
        List<ParticipantEntity> currentParticipants = taskParticipants.getValue();
        currentParticipants.remove(participant);
        taskParticipants.setValue(currentParticipants);
    }


    public LiveData<String> getStatus(long participantId, String date) {
        return repository.getStatus(participantId, date);
    }

    public void updateStatus(long participantId, String date, String status) {
        repository.updateStatus(participantId, date, status);
    }
    public LiveData<List<ParticipantEntity>> getTaskParticipants(long taskId) {
        return repository.getTaskParticipants(taskId);
    }

    public LiveData<List<ParticipantEntity>> getProjectParticipants(long projectId) {
        return repository.getProjectParticipants(projectId);
    }

    public LiveData<List<ParticipantEntity>> getAllParticipants() {
        return allParticipants;
    }

    public LiveData<List<ParticipantEntity>> getProjectAddedParticipants() {
        return projectParticipants;
    }
    public LiveData<List<ParticipantEntity>> getTaskAddedParticipants() {
        return taskParticipants;
    }

}
