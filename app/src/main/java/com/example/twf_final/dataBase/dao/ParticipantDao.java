package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.twf_final.dataBase.entity.ParticipantEntity;

import java.util.List;

@Dao
public interface ParticipantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ParticipantEntity participant);

    @Update
    void update(ParticipantEntity participant);

    @Delete
    void delete(ParticipantEntity participant);

    @Query("SELECT * FROM participants WHERE taskId = :taskId")
    LiveData<List<ParticipantEntity>> getParticipantsForTask(long taskId);

    @Query("SELECT status FROM participants WHERE id = :participantId AND date = :date")
    LiveData<String> getStatus(long participantId, String date);

    @Query("UPDATE participants SET status = :status WHERE id = :participantId AND date = :date")
    void updateStatus(long participantId, String date, String status);

    @Query("SELECT * FROM participants WHERE taskId = :taskId")
    LiveData<List<ParticipantEntity>> getTaskParticipants(long taskId);

    @Query("SELECT * FROM participants WHERE projectId = :projectId")
    LiveData<List<ParticipantEntity>> getProjectParticipants(long projectId);

    @Query("SELECT * FROM participants")
    LiveData<List<ParticipantEntity>> getAllParticipants();
}
