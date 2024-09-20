package com.example.twf_final.dataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class DocumentEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String filePath;

    public DocumentEntity() {

    }
    public DocumentEntity(long id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public DocumentEntity(String filePath) {
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
