package com.example.twf_final.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.dao.TaskDao;
import com.example.twf_final.dataBase.entity.TaskEntity;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<TaskEntity>> allTasks;
    private LiveData<List<TaskEntity>> projectTasks;
    private LiveData<List<TaskEntity>> globalTasks;

    public TaskRepository(Application application) {
        AppDataBase database = AppDataBase.getInstance(application);
        taskDao = database.getTaskDao();
        allTasks = taskDao.getAllTasks();
        projectTasks = taskDao.getProjectTasks();
        globalTasks = taskDao.getGlobalTasks();
    }

    public void insert(TaskEntity task) {
        AppDataBase.databaseWriteExecutor.execute(() -> taskDao.insert(task));
    }

    public void update(TaskEntity task) {
        AppDataBase.databaseWriteExecutor.execute(() -> taskDao.update(task));
    }

    public void delete(TaskEntity task) {
        AppDataBase.databaseWriteExecutor.execute(() -> taskDao.delete(task));
    }

    public LiveData<List<TaskEntity>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<TaskEntity>> getProjectTasks() {
        return projectTasks;
    }

    public LiveData<List<TaskEntity>> getGlobalTasks() {
        return globalTasks;
    }
}
