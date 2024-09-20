package com.example.twf_final.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.entity.UserEntity;
import com.example.twf_final.repository.UserRepository;



public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<UserEntity> user;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        user = userRepository.getUser();
    }


    public LiveData<UserEntity> getUser() {
        return user;
    }

    public void insert(UserEntity userEntity) {
        userRepository.insert(userEntity);
    }

    public void update(UserEntity userEntity) {
        userRepository.update(userEntity);
    }

    public void delete(UserEntity userEntity) {
        userRepository.delete(userEntity);
    }
}
