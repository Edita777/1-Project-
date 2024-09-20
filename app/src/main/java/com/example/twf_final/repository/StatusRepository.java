package com.example.twf_final.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.dao.StatusDao;
import com.example.twf_final.dataBase.entity.StatusEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatusRepository {
    private final StatusDao statusDao;
    private final ExecutorService executorService;

    public StatusRepository(Context context) {
        AppDataBase database = AppDataBase.getInstance(context);
        statusDao = database.getStatusDao();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<String>> getDistinctMonthsForTask(long taskId) {
        return statusDao.getDistinctMonthsForTask(taskId);
    }

    public LiveData<String> getStatusForParticipant(long participantId, String date) {
        return statusDao.getStatusForParticipant(participantId, date);
    }
    public void insertStatus(StatusEntity statusEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> statusDao.insert(statusEntity));
    }
}
