package com.example.twf_final.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twf_final.dataBase.AppDataBase;

import com.example.twf_final.dataBase.dao.UserDao;

import com.example.twf_final.dataBase.entity.UserEntity;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private MutableLiveData<UserEntity> userLiveData = new MutableLiveData<>();

    public UserRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        userDao = db.userDao();
        loadUser();
    }

    private void loadUser() {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            UserEntity user = userDao.getUserNow();
            userLiveData.postValue(user);
        });
    }
    public LiveData<UserEntity> getUser() {
        return userLiveData;
    }

    public void insert(UserEntity userEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            userDao.insert(userEntity);
            loadUser();
        });
    }

    public void update(UserEntity userEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            userDao.update(userEntity);
            loadUser();
        });
    }

    public void delete(UserEntity userEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            userDao.delete(userEntity);
            userLiveData.postValue(null);
        });
    }
}
