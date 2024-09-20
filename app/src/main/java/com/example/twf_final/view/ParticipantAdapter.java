package com.example.twf_final.view;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twf_final.dataBase.entity.ParticipantEntity;

import java.util.List;

import at.wifi.swdev.noteapp.R;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {
    private List<ParticipantEntity> participants;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public ParticipantAdapter(Context context, List<ParticipantEntity> participantItems) {
        this.participants = participantItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item, parent, false);
        return new ParticipantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        ParticipantEntity currentParticipant = participants.get(position);
        holder.roll.setText(String.valueOf(currentParticipant.getRoll()));
        holder.name.setText(currentParticipant.getName());
        holder.company.setText(currentParticipant.getCompany());
        holder.occupation.setText(currentParticipant.getOccupation());
        holder.status.setText(currentParticipant.getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    public void updateParticipants(List<ParticipantEntity> newParticipants) {
        this.participants = newParticipants;
        notifyDataSetChanged();
    }

    private int getColor(int position) {
        String status = participants.get(position).getStatus();
        if ("P".equals(status))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.done)));
        else if ("A".equals(status))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.todo)));
        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.hellgrau)));
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    class ParticipantViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        TextView roll;
        TextView name;
        TextView status;
        TextView company;
        TextView occupation;
        CardView cardView;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status01);
            company = itemView.findViewById(R.id.Company);
            occupation = itemView.findViewById(R.id.Occupation);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 0, 0, "Edit");
            menu.add(getAdapterPosition(), 1, 0, "Delete");
        }
    }
}

