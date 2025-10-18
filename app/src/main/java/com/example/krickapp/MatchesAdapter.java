package com.example.krickapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {

    private List<Match> matchesList;
    private List<Match> matchesListFull; // For filtering
    private OnMatchClickListener listener;

    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }

    public MatchesAdapter(List<Match> matchesList) {
        this.matchesList = matchesList;
        this.matchesListFull = new ArrayList<>(matchesList);
    }

    public void setOnMatchClickListener(OnMatchClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_card_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchesList.get(position);
        holder.bind(match, listener);
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public void filterByStatus(String status) {
        matchesList.clear();
        if (status == null || status.equals("all")) {
            matchesList.addAll(matchesListFull);
        } else {
            for (Match match : matchesListFull) {
                if (match.getStatus() != null && match.getStatus().equalsIgnoreCase(status)) {
                    matchesList.add(match);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(List<Match> newMatches) {
        matchesList.clear();
        matchesListFull.clear();
        matchesList.addAll(newMatches);
        matchesListFull.addAll(newMatches);
        notifyDataSetChanged();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvMatchName, tvTeam1, tvTeam2, tvVenue, tvMatchType, tvDateTime, tvStatus;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatchName = itemView.findViewById(R.id.tv_match_name);
            tvTeam1 = itemView.findViewById(R.id.tv_team1);
            tvTeam2 = itemView.findViewById(R.id.tv_team2);
            tvVenue = itemView.findViewById(R.id.tv_venue);
            tvMatchType = itemView.findViewById(R.id.tv_match_type);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void bind(Match match, OnMatchClickListener listener) {
            tvMatchName.setText(match.getMatchName() != null ? match.getMatchName() : "Unknown Match");
            
            String team1Name = "Team 1";
            String team2Name = "Team 2";
            
            if (match.getTeam1() != null && match.getTeam1().getName() != null) {
                team1Name = match.getTeam1().getName();
            }
            if (match.getTeam2() != null && match.getTeam2().getName() != null) {
                team2Name = match.getTeam2().getName();
            }
            
            tvTeam1.setText(team1Name);
            tvTeam2.setText(team2Name);
            tvVenue.setText(match.getVenue() != null ? match.getVenue() : "Unknown Venue");
            tvMatchType.setText(match.getMatchType() != null ? match.getMatchType() : "N/A");
            
            String dateTime = "";
            if (match.getDate() != null) {
                dateTime = match.getDate();
            }
            if (match.getTime() != null) {
                dateTime += " • " + match.getTime();
            }
            tvDateTime.setText(dateTime);
            
            String status = match.getStatus() != null ? match.getStatus() : "scheduled";
            tvStatus.setText(capitalize(status));
            
            // Set status color
            switch (status.toLowerCase()) {
                case "scheduled":
                    tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
                    break;
                case "ongoing":
                    tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                    break;
                case "completed":
                    tvStatus.setTextColor(Color.parseColor("#2196F3")); // Blue
                    break;
                default:
                    tvStatus.setTextColor(Color.parseColor("#888888")); // Gray
            }
            
            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMatchClick(match);
                }
            });
        }

        private String capitalize(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }
}
