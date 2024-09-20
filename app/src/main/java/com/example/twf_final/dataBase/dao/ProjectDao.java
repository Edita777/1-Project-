package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.twf_final.dataBase.entity.ProjectEntity;
import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void insert(ProjectEntity projectEntity);
    @Update
    void update(ProjectEntity projectEntity);
    @Delete
    void delete(ProjectEntity projectEntity);
    @Query("SELECT * FROM projects")
    LiveData<List<ProjectEntity>> getAllProjects();
}
