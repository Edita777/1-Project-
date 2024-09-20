package com.example.twf_final.dataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "status")
public class StatusEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long participantId;
    private long taskId;
    private String date;
    private String status;

    public StatusEntity() {
    }

    public StatusEntity(long id, long participantId, long taskId, String date, String status) {
        this.id = id;
        this.participantId = participantId;
        this.taskId = taskId;
        this.date = date;
        this.status = status;
    }

    public StatusEntity(long participantId, long taskId, String date, String status) {
        this.participantId = participantId;
        this.taskId = taskId;
        this.date = date;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
