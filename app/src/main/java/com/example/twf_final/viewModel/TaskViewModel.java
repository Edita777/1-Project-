package com.example.twf_final.viewModel;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.repository.TaskRepository;
import com.example.twf_final.utils.TaskDueReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<TaskEntity>> allTasks;
    private MutableLiveData<List<TaskEntity>> projectTasks;
    private MutableLiveData<List<TaskEntity>> globalTasks;
    private MutableLiveData<List<TaskEntity>> notifications;
    private MutableLiveData<TaskEntity> taskStatusChangedNotification = new MutableLiveData<>();
    private MutableLiveData<List<TaskEntity>> dueDateNotifications;
    private Context context;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        projectTasks = new MutableLiveData<>(new ArrayList<>());
        globalTasks = new MutableLiveData<>(new ArrayList<>());
        notifications = new MutableLiveData<>(new ArrayList<>());
        dueDateNotifications = new MutableLiveData<>(new ArrayList<>());
        loadTasks();
    }

    private void loadTasks() {
        repository.getAllTasks().observeForever(tasks -> {
            List<TaskEntity> globalList = new ArrayList<>();
            List<TaskEntity> projectList = new ArrayList<>();
            List<TaskEntity> dueDateList = new ArrayList<>();
            long currentTime = System.currentTimeMillis();
            long oneDayMillis = TimeUnit.DAYS.toMillis(1);
            for (TaskEntity task : tasks) {
                if (task.getProjectId() == 0) {
                    globalList.add(task);
                } else {
                    projectList.add(task);
                }
                // Check if due date is within 24 hours and add to dueDateNotifications
                if (task.getDueDateMillis() - currentTime <= oneDayMillis && task.getDueDateMillis() >= currentTime) {
                    dueDateList.add(task);
                }
            }
            globalTasks.setValue(globalList);
            projectTasks.setValue(projectList);
            dueDateNotifications.setValue(dueDateList);
        });
    }

    public void insert(TaskEntity task, boolean isProjectTask) {
        repository.insert(task);
        if (isProjectTask) {
            addProjectTask(task);
        } else {
            addGlobalTask(task);
        }
        addNotification(task);
        setTaskDueAlarm(task);
    }

    public void update(TaskEntity task) {
        repository.update(task);
        updateProjectTask(task);
        updateGlobalTask(task);
        setTaskDueAlarm(task);
    }

    public void delete(TaskEntity task) {
        repository.delete(task);
        removeProjectTask(task);
        removeGlobalTask(task);
        removeNotification(task);
        removeDueDateNotification(task);
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

    public LiveData<List<TaskEntity>> getNotifications() {
        return notifications;
    }

    public LiveData<List<TaskEntity>> getDueDateNotifications() {
        return dueDateNotifications;
    }

    public LiveData<TaskEntity> getTaskStatusChangedNotification() {
        return taskStatusChangedNotification;
    }

    public void notifyTaskStatusChanged(TaskEntity task) {
        taskStatusChangedNotification.setValue(task);
    }

    private void addProjectTask(TaskEntity task) {
        List<TaskEntity> currentTasks = projectTasks.getValue();
        if (currentTasks == null) {
            currentTasks = new ArrayList<>();
        }
        if (!currentTasks.contains(task)) {
            currentTasks.add(task);
            projectTasks.postValue(currentTasks);
        }
    }

    private void addGlobalTask(TaskEntity task) {
        List<TaskEntity> currentTasks = globalTasks.getValue();
        if (currentTasks == null) {
            currentTasks = new ArrayList<>();
        }
        if (!currentTasks.contains(task)) {
            currentTasks.add(task);
            globalTasks.postValue(currentTasks);
        }
    }

    private void updateProjectTask(TaskEntity task) {
        List<TaskEntity> currentTasks = projectTasks.getValue();
        if (currentTasks == null) {
            return;
        }
        int index = currentTasks.indexOf(task);
        if (index != -1) {
            currentTasks.set(index, task);
            projectTasks.postValue(currentTasks);
        }
    }

    private void updateGlobalTask(TaskEntity task) {
        List<TaskEntity> currentTasks = globalTasks.getValue();
        if (currentTasks == null) {
            return;
        }
        int index = currentTasks.indexOf(task);
        if (index != -1) {
            currentTasks.set(index, task);
            globalTasks.postValue(currentTasks);
        }
    }

    private void removeProjectTask(TaskEntity task) {
        List<TaskEntity> currentTasks = projectTasks.getValue();
        if (currentTasks == null) {
            return;
        }
        currentTasks.remove(task);
        projectTasks.postValue(currentTasks);
    }

    private void removeGlobalTask(TaskEntity task) {
        List<TaskEntity> currentTasks = globalTasks.getValue();
        if (currentTasks == null) {
            return;
        }
        currentTasks.remove(task);
        globalTasks.postValue(currentTasks);
    }

    public void addNotification(TaskEntity task) {
        List<TaskEntity> currentNotifications = notifications.getValue();
        if (currentNotifications == null) {
            currentNotifications = new ArrayList<>();
        }
        currentNotifications.add(task);
        notifications.postValue(currentNotifications);
    }

    private void removeNotification(TaskEntity task) {
        List<TaskEntity> currentNotifications = notifications.getValue();
        if (currentNotifications == null) {
            return;
        }
        currentNotifications.remove(task);
        notifications.postValue(currentNotifications);
    }

    public void addDueDateNotification(TaskEntity task) {
        List<TaskEntity> currentNotifications = dueDateNotifications.getValue();
        if (currentNotifications == null) {
            currentNotifications = new ArrayList<>();
        }
        currentNotifications.add(task);
        dueDateNotifications.postValue(currentNotifications);
    }

    public void removeDueDateNotification(TaskEntity task) {
        List<TaskEntity> currentDueDateNotifications = dueDateNotifications.getValue();
        if (currentDueDateNotifications != null) {
            currentDueDateNotifications.remove(task);
            dueDateNotifications.setValue(currentDueDateNotifications);
        }
    }

    public void clearNotifications() {
        notifications.postValue(new ArrayList<>());
    }

    public TaskEntity getTaskById(long taskId) {
        List<TaskEntity> tasks = allTasks.getValue();
        if (tasks != null) {
            for (TaskEntity task : tasks) {
                if (task.getId() == taskId) {
                    return task;
                }
            }
        }
        return null;
    }

    public void setTaskDueAlarm(TaskEntity task) {
        long dueDateMillis = task.getDueDateMillis() - TimeUnit.DAYS.toMillis(1); // 24 hours before due date
        Intent intent = new Intent(context, TaskDueReceiver.class);
        intent.putExtra("taskId", task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dueDateMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dueDateMillis, pendingIntent);
            }
        }
    }

    public void setRecurringTaskDueAlarm(TaskEntity task) {
        long startMillis = task.getDueDateMillis() - TimeUnit.DAYS.toMillis(1);
        long intervalMillis = TimeUnit.MINUTES.toMillis(2);

        Intent intent = new Intent(context, TaskDueReceiver.class);
        intent.putExtra("taskId", task.getId());
        intent.putExtra("taskName", task.getTaskName());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startMillis, intervalMillis, pendingIntent);
        }
    }
}
