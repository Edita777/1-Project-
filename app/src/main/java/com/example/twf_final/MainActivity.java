package com.example.twf_final;

import static com.example.twf_final.view.ItemFragment.PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.os.Handler;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.twf_final.dataBase.entity.ProjectEntity;
import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.utils.NotificationUtils;
import com.example.twf_final.view.AccountFragment;
import com.example.twf_final.view.ItemFragment;
import com.example.twf_final.view.NotificationFragment;
import com.example.twf_final.view.ProjectFragment;
import com.example.twf_final.view.ReminderDialogFragment;
import com.example.twf_final.view.SearchFragment;
import com.example.twf_final.view.TaskFragment;
import com.example.twf_final.viewModel.ProjectViewModel;
import com.example.twf_final.viewModel.TaskViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import at.wifi.swdev.noteapp.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView navigationBarView;
    private ProjectViewModel projectViewModel;
    private TaskViewModel taskViewModel;
    private Handler reminderHandler;
    private Runnable reminderRunnable;
    private static final long REMINDER_INTERVAL = 2 * 60 * 1000; // 2 Minuten
    private NotificationFragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBarView = findViewById(R.id.dashboard);

        projectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        setupBottomMenu();
        displayFragment(new ProjectFragment());

        projectViewModel.getNotifications().observe(this, notifications -> {
            updateNotificationBadge();
        });

        taskViewModel.getNotifications().observe(this, taskNotifications -> {
            updateNotificationBadge();
        });

        taskViewModel.getDueDateNotifications().observe(this, dueDateNotifications -> {
            updateNotificationBadge();
        });

        NotificationUtils.createNotificationChannel(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkAndRequestPermissions();
        }

        reminderHandler = new Handler();
        reminderRunnable = new Runnable() {
            @Override
            public void run() {
                showReminderDialog();
                reminderHandler.postDelayed(this, REMINDER_INTERVAL);
            }
        };

        // Starte das Runnable
        reminderHandler.post(reminderRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reminderHandler.removeCallbacks(reminderRunnable);
    }

    private void showReminderDialog() {
        TaskEntity nextDueTask = getNextDueTask();
        if (nextDueTask != null) {
            ReminderDialogFragment reminderDialog = ReminderDialogFragment.newInstance(nextDueTask.getTaskName());
            reminderDialog.show(getSupportFragmentManager(), "reminderDialog");
        }
    }

    private TaskEntity getNextDueTask() {
        List<TaskEntity> tasks = taskViewModel.getDueDateNotifications().getValue();
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.get(0);
        }
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_project || itemId == R.id.nav_task || itemId == R.id.nav_account || itemId == R.id.nav_search) {
            clearProjectAndTaskNotifications();
            return displayFragment(getFragmentByMenuItemId(itemId));
        } else if (itemId == R.id.nav_notification) {
            clearNotificationBadge();
            notificationFragment = new NotificationFragment();
            return displayFragment(notificationFragment);
        }
        return false;
    }

    private Fragment getFragmentByMenuItemId(int itemId) {
        if (itemId == R.id.nav_project) {
            return new ProjectFragment();
        } else if (itemId == R.id.nav_task) {
            return new TaskFragment();
        } else if (itemId == R.id.nav_account) {
            return new AccountFragment();
        } else if (itemId == R.id.nav_search) {
            return new SearchFragment();
        } else if (itemId == R.id.nav_notification) {
            return notificationFragment;
        } else {
            return null;
        }
    }

    private void setupBottomMenu() {
        navigationBarView.setOnItemSelectedListener(this::onNavigationItemSelected);
        navigationBarView.setSelectedItemId(R.id.nav_project);
    }

    private boolean displayFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void updateNotificationBadge() {
        int projectCount = projectViewModel.getNotifications().getValue() != null ? projectViewModel.getNotifications().getValue().size() : 0;
        int taskCount = taskViewModel.getNotifications().getValue() != null ? taskViewModel.getNotifications().getValue().size() : 0;
        int dueDateCount = taskViewModel.getDueDateNotifications().getValue() != null ? taskViewModel.getDueDateNotifications().getValue().size() : 0;

        int totalCount = projectCount + taskCount + dueDateCount;

        BadgeDrawable badge = navigationBarView.getOrCreateBadge(R.id.nav_notification);
        if (totalCount > 0) {
            badge.setVisible(true);
            badge.setNumber(totalCount);
        } else {
            badge.setVisible(false);
        }
    }

    public void clearNotificationBadge() {
        BadgeDrawable badge = navigationBarView.getBadge(R.id.nav_notification);
        if (badge != null) {
            badge.setVisible(false);
        }
    }

    public void gotoItemFragment(ProjectEntity projectEntity, int position) {
        Bundle bundle = new Bundle();
        bundle.putLong("ProjectNumber", projectEntity.getProjectNumber());
        bundle.putString("ProjectName", projectEntity.getProjectName());
        bundle.putString("Description", projectEntity.getDescription());
        bundle.putInt("position", position);
        bundle.putLong("id", projectEntity.getId());

        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, itemFragment)
                .addToBackStack(null)
                .commit();
    }

    public void gotoItemFragment(TaskEntity taskEntity, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("TaskName", taskEntity.getTaskName());
        bundle.putString("SubjectName", taskEntity.getSubjectName());
        bundle.putString("Status", taskEntity.getStatus());
        bundle.putInt("position", position);
        bundle.putLong("id", taskEntity.getId());

        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, itemFragment)
                .addToBackStack(null)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Berechtigungen erteilt
            } else {
                // Berechtigungen nicht erteilt
            }
        }
    }

    private void clearProjectAndTaskNotifications() {
        projectViewModel.clearNotifications();
        taskViewModel.clearNotifications();
    }
}
