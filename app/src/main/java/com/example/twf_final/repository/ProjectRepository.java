package com.example.twf_final.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.dao.ProjectDao;
import com.example.twf_final.dataBase.entity.ProjectEntity;

import java.util.List;

public class ProjectRepository {

    private ProjectDao projectDao;
    private LiveData<List<ProjectEntity>> allProjects;

    public ProjectRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        projectDao = db.getProjectDao();
        allProjects = projectDao.getAllProjects();
    }

    public LiveData<List<ProjectEntity>> getAllProjects() {
        return allProjects;
    }

    public void insert(ProjectEntity projectEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> projectDao.insert(projectEntity));
    }

    public void update(ProjectEntity projectEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> projectDao.update(projectEntity));
    }

    public void delete(ProjectEntity projectEntity) {
        AppDataBase.databaseWriteExecutor.execute(() -> projectDao.delete(projectEntity));
    }
}
