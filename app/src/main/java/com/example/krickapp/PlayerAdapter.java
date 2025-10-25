package com.example.krickapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<String> players;
    private OnPlayerClickListener listener;
    private OnPlayerLongClickListener longClickListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public interface OnPlayerClickListener {
        void onPlayerClick(int position, String playerName);
    }

    public interface OnPlayerLongClickListener {
        void onPlayerLongClick(int position, String playerName);
    }

    public PlayerAdapter(List<String> players) {
        this.players = players;
    }

    public void setOnPlayerClickListener(OnPlayerClickListener listener) {
        this.listener = listener;
    }

    public void setOnPlayerLongClickListener(OnPlayerLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        String playerName = players.get(position);
        holder.tvPlayerName.setText(playerName);

        // Selection visual: if selected show slightly darker background
        if (position == selectedPos) {
            holder.card.setCardBackgroundColor(0xFFF7F7F7); // very light grey
        } else {
            holder.card.setCardBackgroundColor(0xFFFFFFFF); // white
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPos = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPos);
            if (listener != null) {
                listener.onPlayerClick(selectedPos, playerName);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onPlayerLongClick(holder.getAdapterPosition(), playerName);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerName;
        ImageView imgAvatar;
        CardView card;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            card = (CardView) itemView;
        }
    }
}
