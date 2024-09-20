package com.example.twf_final.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twf_final.dataBase.entity.ProjectEntity;
import com.example.twf_final.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

public class ProjectViewModel extends AndroidViewModel {

    private ProjectRepository projectRepository;
    private LiveData<List<ProjectEntity>> allProjects;
    private MutableLiveData<List<ProjectEntity>> notifications;
    private MutableLiveData<List<ProjectEntity>> allProjectNotifications;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        projectRepository = new ProjectRepository(application);
        allProjects = projectRepository.getAllProjects();
        notifications = new MutableLiveData<>(new ArrayList<>());
        allProjectNotifications = new MutableLiveData<>(new ArrayList<>());

        allProjects.observeForever(projectEntities -> {
            List<ProjectEntity> notificationList = new ArrayList<>();
            for (ProjectEntity project : projectEntities) {
                if (project.isClicked()) {
                    notificationList.add(project);
                }
            }
            allProjectNotifications.postValue(notificationList);
        });
    }

    public LiveData<List<ProjectEntity>> getAllProjects() {
        return allProjects;
    }

    public LiveData<List<ProjectEntity>> getNotifications() {
        return notifications;
    }

    public LiveData<List<ProjectEntity>> getAllProjectNotifications() {
        return allProjectNotifications;
    }

    public void insert(ProjectEntity projectEntity) {
        projectRepository.insert(projectEntity);
        addNotification(projectEntity);
    }

    public void update(ProjectEntity projectEntity) {
        projectRepository.update(projectEntity);
        updateNotification(projectEntity);
    }

    public void delete(ProjectEntity projectEntity) {
        projectRepository.delete(projectEntity);
        removeNotification(projectEntity);
    }

    public void addNotification(ProjectEntity projectEntity) {
        List<ProjectEntity> currentNotifications = notifications.getValue();
        if (currentNotifications != null && !currentNotifications.contains(projectEntity)) {
            currentNotifications.add(projectEntity);
            notifications.postValue(currentNotifications);
        }
    }

    public void updateNotification(ProjectEntity projectEntity) {
        List<ProjectEntity> currentNotifications = notifications.getValue();
        if (currentNotifications != null) {
            int index = currentNotifications.indexOf(projectEntity);
            if (index >= 0) {
                currentNotifications.set(index, projectEntity);
                notifications.postValue(currentNotifications);
            }
        }
    }

    public void removeNotification(ProjectEntity projectEntity) {
        List<ProjectEntity> currentNotifications = notifications.getValue();
        if (currentNotifications != null) {
            currentNotifications.remove(projectEntity);
            notifications.postValue(currentNotifications);
        }
    }

    public void clearNotifications() {
        notifications.postValue(new ArrayList<>());
    }
}
