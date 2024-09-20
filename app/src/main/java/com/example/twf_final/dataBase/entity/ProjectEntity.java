package com.example.twf_final.dataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "projects")
public class ProjectEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long projectNumber;
    private String projectName;
    private String description;
    private boolean clicked;

    public ProjectEntity() {
    }

    public ProjectEntity(long projectNumber, String projectName, String description) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.description = description;
        this.clicked = false;
    }

    public ProjectEntity(long projectNumber, String projectName, String description, boolean clicked) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.description = description;
        this.clicked = clicked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(long projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
