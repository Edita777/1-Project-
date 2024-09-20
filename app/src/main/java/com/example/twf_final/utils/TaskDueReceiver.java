package com.example.twf_final.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentActivity;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.view.ReminderDialogFragment;

public class TaskDueReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long taskId = intent.getLongExtra("taskId", -1);
        String taskName = intent.getStringExtra("taskName");

        if (taskId != -1 && taskName != null) {
            new FetchTaskAsyncTask(context, taskId, taskName).execute();
        }
    }

    private static class FetchTaskAsyncTask extends AsyncTask<Void, Void, TaskEntity> {
        private Context context;
        private long taskId;
        private String taskName;

        FetchTaskAsyncTask(Context context, long taskId, String taskName) {
            this.context = context.getApplicationContext();
            this.taskId = taskId;
            this.taskName = taskName;
        }

        @Override
        protected TaskEntity doInBackground(Void... voids) {
            AppDataBase db = AppDataBase.getInstance(context);
            return db.getTaskDao().getTaskById(taskId);
        }

        @Override
        protected void onPostExecute(TaskEntity task) {
            if (task != null) {
                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;
                    ReminderDialogFragment dialog = ReminderDialogFragment.newInstance(taskName);
                    dialog.show(activity.getSupportFragmentManager(), "ReminderDialog");
                }
            }
        }
    }
}
