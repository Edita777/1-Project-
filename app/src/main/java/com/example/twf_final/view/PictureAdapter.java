package com.example.twf_final.view;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import at.wifi.swdev.noteapp.R;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {


    private List<File> pictures;

    public PictureAdapter(List<File> pictures) {
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        return new PictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        File picture = pictures.get(position);
        holder.imageView.setImageURI(Uri.fromFile(picture));
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class PictureViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        PictureViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}