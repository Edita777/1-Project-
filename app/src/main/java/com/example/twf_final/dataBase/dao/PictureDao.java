package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.twf_final.dataBase.entity.PictureEntity;

import java.util.List;

@Dao
public interface PictureDao {
    @Insert
    void insert(PictureEntity picture);

    @Update
    void update(PictureEntity picture);

    @Delete
    void delete(PictureEntity picture);

    @Query("SELECT * FROM pictures")
    LiveData<List<PictureEntity>> getAllPictures();
}
