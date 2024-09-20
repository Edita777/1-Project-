package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.twf_final.dataBase.entity.StatusEntity;

import java.util.List;

@Dao
public interface StatusDao {

    @Query("SELECT DISTINCT substr(date, 4) AS month FROM status WHERE taskId = :taskId ORDER BY month")
    LiveData<List<String>> getDistinctMonthsForTask(long taskId);

    @Query("SELECT status FROM status WHERE participantId = :participantId AND date = :date")
    LiveData<String> getStatusForParticipant(long participantId, String date);

    @Insert
    void insert(StatusEntity statusEntity);
}
