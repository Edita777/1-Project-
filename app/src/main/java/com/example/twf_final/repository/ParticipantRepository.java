package com.example.twf_final.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.dao.ParticipantDao;
import com.example.twf_final.dataBase.entity.ParticipantEntity;
import com.example.twf_final.dataBase.entity.TaskEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParticipantRepository {

    private ParticipantDao participantDao;
    private LiveData<List<ParticipantEntity>>allParticipants;
    private ExecutorService executorService;

    public ParticipantRepository(Application application) {
        AppDataBase database = AppDataBase.getInstance(application);
        participantDao = database.getParticipantDao();
        allParticipants =participantDao.getAllParticipants();
        executorService = Executors.newFixedThreadPool(3);

    }

    public void insert(ParticipantEntity participant) {
        AppDataBase.databaseWriteExecutor.execute(() -> participantDao.insert(participant));
    }
    public void update(ParticipantEntity participant) {
        AppDataBase.databaseWriteExecutor.execute(() -> participantDao.update(participant));
    }
    public void delete(ParticipantEntity participant) {
        AppDataBase.databaseWriteExecutor.execute(() -> participantDao.delete(participant));
    }

    public LiveData<List<ParticipantEntity>> getTaskParticipants(long taskId) {
        return participantDao.getTaskParticipants(taskId);
    }

    public LiveData<List<ParticipantEntity>> getProjectParticipants(long projectId) {
        return participantDao.getProjectParticipants(projectId);
    }
    public LiveData<List<ParticipantEntity>>
    getAllParticipants() {
        return allParticipants;
    }

    public LiveData<String> getStatus(long participantId, String date) {
        return participantDao.getStatus(participantId, date);
    }
    public void updateStatus(long participantId, String date, String status) {
        AppDataBase.databaseWriteExecutor.execute(() -> participantDao.updateStatus(participantId, date, status));
    }
}
