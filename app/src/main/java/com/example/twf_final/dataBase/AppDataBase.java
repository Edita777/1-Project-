package com.example.twf_final.dataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.twf_final.dataBase.dao.DocumentDao;
import com.example.twf_final.dataBase.dao.ParticipantDao;
import com.example.twf_final.dataBase.dao.PictureDao;
import com.example.twf_final.dataBase.dao.ProjectDao;
import com.example.twf_final.dataBase.dao.StatusDao;
import com.example.twf_final.dataBase.dao.TaskDao;
import com.example.twf_final.dataBase.dao.UserDao;
import com.example.twf_final.dataBase.entity.DocumentEntity;
import com.example.twf_final.dataBase.entity.ParticipantEntity;
import com.example.twf_final.dataBase.entity.PictureEntity;
import com.example.twf_final.dataBase.entity.ProjectEntity;
import com.example.twf_final.dataBase.entity.StatusEntity;
import com.example.twf_final.dataBase.entity.TaskEntity;
import com.example.twf_final.dataBase.entity.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        TaskEntity.class,
        ParticipantEntity.class,
        StatusEntity.class,
        UserEntity.class,
        ProjectEntity.class,
        PictureEntity.class,
        DocumentEntity.class},
        version = 18, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase INSTANCE;
    public static final int NUMBER_OF_THREADS = 5;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract TaskDao getTaskDao();
    public abstract PictureDao getPictureDao();
    public abstract DocumentDao getDocumentDao();
    public abstract ParticipantDao getParticipantDao();
    public abstract StatusDao getStatusDao();
    public abstract UserDao userDao();
    public abstract ProjectDao getProjectDao();

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "twf_final_database")
                            .addMigrations(MIGRATION_9_10)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE participants ADD COLUMN date TEXT");
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE status (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "participantId INTEGER NOT NULL, date TEXT, status TEXT)");
        }
    };


}
