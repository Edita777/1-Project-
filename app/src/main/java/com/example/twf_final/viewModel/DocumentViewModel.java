package com.example.twf_final.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.twf_final.dataBase.entity.DocumentEntity;
import com.example.twf_final.repository.DocumentRepository;
import java.util.ArrayList;
import java.util.List;

public class DocumentViewModel extends AndroidViewModel {

    private DocumentRepository repository;
    private LiveData<List<DocumentEntity>> allDocuments;
    private MutableLiveData<List<DocumentEntity>> projectDocuments = new MutableLiveData<>(new ArrayList<>());

    public DocumentViewModel(@NonNull Application application) {
        super(application);
        repository = new DocumentRepository(application);
        allDocuments = repository.getAllDocuments();
    }

    public void insertDocument(DocumentEntity document) {
        repository.insert(document);
        List<DocumentEntity> currentDocuments = projectDocuments.getValue();
        if (currentDocuments != null && !currentDocuments.contains(document)) {
            currentDocuments.add(document);
            projectDocuments.setValue(currentDocuments);
        }
    }

    public void deleteDocument(DocumentEntity document) {
        repository.delete(document);
        List<DocumentEntity> currentDocuments = projectDocuments.getValue();
        if (currentDocuments != null) {
            currentDocuments.remove(document);
            projectDocuments.setValue(currentDocuments);
        }
    }

    public LiveData<List<DocumentEntity>> getAllDocuments() {
        return allDocuments;
    }

    public LiveData<List<DocumentEntity>> getProjectDocuments() {
        return projectDocuments;
    }
}
