package com.example.twf_final.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.twf_final.dataBase.AppDataBase;
import com.example.twf_final.dataBase.dao.DocumentDao;
import com.example.twf_final.dataBase.entity.DocumentEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DocumentRepository {

    private DocumentDao documentDao;
    private LiveData<List<DocumentEntity>> allDocuments;
    private ExecutorService executorService;

    public DocumentRepository(Application application) {
        AppDataBase db = AppDataBase.getInstance(application);
        documentDao = db.getDocumentDao();
        allDocuments = documentDao.getAllDocuments();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<DocumentEntity>> getAllDocuments() {
        return allDocuments;
    }

    public void insert(DocumentEntity documentEntity) {
        executorService.execute(() -> documentDao.insert(documentEntity));
    }

    public void update(DocumentEntity documentEntity) {
        executorService.execute(() -> documentDao.update(documentEntity));
    }

    public void delete(DocumentEntity documentEntity) {
        executorService.execute(() -> documentDao.delete(documentEntity));
    }
}
