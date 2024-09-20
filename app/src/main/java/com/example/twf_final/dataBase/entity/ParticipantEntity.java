package com.example.twf_final.dataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "participants")
public class ParticipantEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int roll;
    private String name;
    private String status;
    private long taskId;
    private String company;
    private String occupation;
    private long projectId;
    private String date;

    public ParticipantEntity(long taskId, int roll, String name) {
        this.taskId = taskId;
        this.roll = roll;
        this.name = name;
        this.status = "";
        this.date = "";
    }
    public ParticipantEntity(long id, int roll, String name, long taskId) {
        this.id = id;
        this.roll = roll;
        this.name = name;
        this.status = "";
        this.taskId = taskId;
        this.date = "";
    }

    public ParticipantEntity(int roll, String name,String company, String occupation, long taskId) {
        this.roll = roll;
        this.name = name;
        this.status = "";
        this.taskId = taskId;
        this.company = company;
        this.occupation = occupation;
        this.date = "";
    }

    public ParticipantEntity(int roll, String name, String status, long taskId, long projectId, String date) {
        this.roll = roll;
        this.name = name;
        this.status = status;
        this.taskId = taskId;
        this.projectId = projectId;
        this.date = date;
    }

    public ParticipantEntity() {

    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public long getProjectId() {
        return projectId;
    }
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
