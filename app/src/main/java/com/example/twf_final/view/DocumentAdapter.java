package com.example.twf_final.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import at.wifi.swdev.noteapp.R;
import java.io.File;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private List<File> documentList;
    private OnDocumentActionListener listener;

    public interface OnDocumentActionListener {
        void onDelete(File document);
        void onUpdate(File document);
    }

    public DocumentAdapter(List<File> documentList, OnDocumentActionListener listener) {
        this.documentList = documentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_item, parent, false);
        return new DocumentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        File document = documentList.get(position);
        holder.documentTextView.setText(document.getName());

        holder.editButton.setOnClickListener(v -> listener.onUpdate(document));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(document));
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView documentTextView;
        ImageButton editButton;
        ImageButton deleteButton;

        DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            documentTextView = itemView.findViewById(R.id.documentTextView);
            editButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
