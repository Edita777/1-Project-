package com.example.twf_final.dataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "tasks")
public class TaskEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String taskName;
    private String subjectName;
    private String status;
    private boolean clicked;
    private String creationDate;
    private String dueDate;
    private String dueTime;
    private long projectId;

    public TaskEntity(long id, String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime, long projectId) {
        this.id = id;
        this.taskName = taskName;
        this.subjectName = subjectName;
        this.status = status;
        this.clicked = false;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.projectId = projectId;
    }

    public TaskEntity() {
    }

    public TaskEntity(String taskName, String subjectName, String status, String creationDate, String dueDate, String dueTime, long projectId) {
        this.taskName = taskName;
        this.subjectName = subjectName;
        this.status = status;
        this.clicked = false;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.projectId = projectId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public long getCreationDateMillis() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = format.parse(this.creationDate);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getDueDateMillis() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date date = format.parse(this.dueDate + " " + this.dueTime);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
