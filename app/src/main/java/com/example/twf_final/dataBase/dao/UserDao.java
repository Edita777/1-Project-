package com.example.twf_final.dataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.dataBase.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

    @Delete
    void delete(UserEntity userEntity);

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserEntity> getUser();

    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getUserNow();
}