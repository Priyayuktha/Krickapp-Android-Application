package com.example.krickapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.VH> {

    private List<String> items;
    private int selectedPos = RecyclerView.NO_POSITION;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, String name);
    }

    public void setOnItemClickListener(OnItemClickListener l) { this.listener = l; }

    public PlayerAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        String name = items.get(position);
        holder.tvName.setText(name);

        // selection visual: if selected show slightly darker background
        if (position == selectedPos) {
            holder.card.setCardBackgroundColor(0xFFF7F7F7); // very light
            holder.imgAvatar.setImageResource(R.drawable.circle_grey); // you can switch to selected drawable
        } else {
            holder.card.setCardBackgroundColor(0xFFFFFFFF);
            holder.imgAvatar.setImageResource(R.drawable.circle_grey);
        }

        holder.itemView.setOnClickListener(v -> {
            int old = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(old);
            notifyItemChanged(selectedPos);
            if (listener != null) listener.onItemClick(selectedPos, name);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgAvatar;
        CardView card;
        VH(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPlayerName);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            card = (CardView) itemView;
        }
    }
}
