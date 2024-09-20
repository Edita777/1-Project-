package com.example.twf_final.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory{
    private Application application;

    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ProjectViewModel.class)) {
            return (T) new ProjectViewModel(application);
        }

        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(application);
        }

        if (modelClass.isAssignableFrom(ParticipantViewModel.class)) {
            return (T) new ProjectViewModel(application);
        }

        if (modelClass.isAssignableFrom(StatusViewModel.class)) {
            return (T) new StatusViewModel(application);
        }


        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(application);
        }

        if (modelClass.isAssignableFrom(DocumentViewModel.class)) {
            return (T) new DocumentViewModel(application);
        }


        return ViewModelProvider.Factory.super.create(modelClass);
    }


}
