package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.twf_final.dataBase.entity.DocumentEntity;

import java.util.List;

@Dao
public interface DocumentDao {
    @Insert
    void insert(DocumentEntity document);

    @Update
    void update(DocumentEntity document);

    @Delete
    void delete(DocumentEntity document);

    @Query("SELECT * FROM documents")
    LiveData<List<DocumentEntity>> getAllDocuments();
}
