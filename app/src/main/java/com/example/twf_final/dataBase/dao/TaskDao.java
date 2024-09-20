package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.twf_final.dataBase.entity.TaskEntity;

import java.util.List;


@Dao
public interface TaskDao {
    @Insert
    long insert(TaskEntity task);

    @Update
    void update(TaskEntity task);

    @Delete
    void delete(TaskEntity task);

    @Query("SELECT * FROM tasks")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE projectId != 0")
    LiveData<List<TaskEntity>> getProjectTasks();

    @Query("SELECT * FROM tasks WHERE projectId = 0")
    LiveData<List<TaskEntity>> getGlobalTasks();


    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    TaskEntity getTaskById(long taskId);
}


