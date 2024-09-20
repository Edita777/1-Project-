package com.example.twf_final.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.entity.StatusEntity;
import com.example.twf_final.repository.StatusRepository;

import java.util.List;

public class StatusViewModel extends AndroidViewModel {
    private StatusRepository statusRepository;

    public StatusViewModel(@NonNull Application application) {
        super(application);
        statusRepository = new StatusRepository(application);
    }

    public LiveData<List<String>> getDistinctMonthsForTask(long taskId) {
        return statusRepository.getDistinctMonthsForTask(taskId);
    }

    public LiveData<String> getStatusForParticipant(long participantId, String date) {
        return statusRepository.getStatusForParticipant(participantId, date);
    }

    public void insertStatus(StatusEntity statusEntity) {
        statusRepository.insertStatus(statusEntity);
    }
}
