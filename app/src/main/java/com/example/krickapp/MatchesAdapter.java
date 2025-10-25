package com.example.krickapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {

    private List<Match> matchesList;
    private Context context;

    public MatchesAdapter(List<Match> matchesList, Context context) {
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match_card, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchesList.get(position);
        holder.bind(match, context);
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatusBadge, tvDate, tvStatusText, tvVenue;
        TextView tvTeam1Name, tvTeam1Score, tvTeam1Overs;
        TextView tvTeam2Name, tvTeam2Score, tvTeam2Overs;
        TextView tvVs, tvMatchInfo;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatusBadge = itemView.findViewById(R.id.tv_status_badge);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvStatusText = itemView.findViewById(R.id.tv_status_text);
            tvVenue = itemView.findViewById(R.id.tv_venue);
            
            tvTeam1Name = itemView.findViewById(R.id.tv_team1_name);
            tvTeam1Score = itemView.findViewById(R.id.tv_team1_score);
            tvTeam1Overs = itemView.findViewById(R.id.tv_team1_overs);
            
            tvTeam2Name = itemView.findViewById(R.id.tv_team2_name);
            tvTeam2Score = itemView.findViewById(R.id.tv_team2_score);
            tvTeam2Overs = itemView.findViewById(R.id.tv_team2_overs);
            
            tvVs = itemView.findViewById(R.id.tv_vs);
            tvMatchInfo = itemView.findViewById(R.id.tv_match_info);
        }

        public void bind(Match match, Context context) {
            String status = match.getStatus() != null ? match.getStatus().toLowerCase() : "scheduled";
            
            // Set venue
            tvVenue.setText(match.getVenue() != null ? match.getVenue() : "Unknown Venue");
            
            // Set date
            tvDate.setText(match.getDate() != null ? match.getDate() : "TBD");
            
            // Get team names
            String team1Name = "Team 1";
            String team2Name = "Team 2";
            
            if (match.getTeam1() != null && match.getTeam1().getTeamName() != null) {
                team1Name = match.getTeam1().getTeamName();
            }
            if (match.getTeam2() != null && match.getTeam2().getTeamName() != null) {
                team2Name = match.getTeam2().getTeamName();
            }
            
            tvTeam1Name.setText(team1Name);
            tvTeam2Name.setText(team2Name);
            
            // Configure based on status
            if (status.equals("scheduled")) {
                // Scheduled match
                tvStatusBadge.setText("Scheduled");
                tvStatusBadge.setBackgroundResource(R.drawable.status_scheduled_background);
                tvStatusText.setText("Scheduled");
                tvStatusText.setTextColor(context.getColor(R.color.status_scheduled));
                
                // Hide scores, show VS
                tvTeam1Score.setVisibility(View.GONE);
                tvTeam1Overs.setVisibility(View.GONE);
                tvTeam2Score.setVisibility(View.GONE);
                tvTeam2Overs.setVisibility(View.GONE);
                
                tvVs.setVisibility(View.VISIBLE);
                tvMatchInfo.setVisibility(View.VISIBLE);
                tvMatchInfo.setText("Starts in 1h 55m"); // TODO: Calculate actual time
                
            } else if (status.equals("ongoing") || status.equals("live")) {
                // Live match
                tvStatusBadge.setText("Live");
                tvStatusBadge.setBackgroundResource(R.drawable.status_live_background);
                tvStatusText.setText("Live");
                tvStatusText.setTextColor(context.getColor(R.color.status_live));
                
                // Show scores
                tvTeam1Score.setVisibility(View.VISIBLE);
                tvTeam1Overs.setVisibility(View.VISIBLE);
                tvTeam2Score.setVisibility(View.VISIBLE);
                tvTeam2Overs.setVisibility(View.VISIBLE);
                
                tvVs.setVisibility(View.GONE);
                tvMatchInfo.setVisibility(View.GONE);
                
                // Set scores (mock data for now)
                tvTeam1Score.setText("298-7");
                tvTeam1Overs.setText("(47.3)");
                tvTeam2Score.setText("156-3");
                tvTeam2Overs.setText("(28.1)");
                
            } else if (status.equals("completed")) {
                // Completed match
                tvStatusBadge.setText("Completed");
                tvStatusBadge.setBackgroundResource(R.drawable.status_completed_background);
                tvStatusText.setText("Completed");
                tvStatusText.setTextColor(context.getColor(R.color.status_completed));
                
                // Show scores
                tvTeam1Score.setVisibility(View.VISIBLE);
                tvTeam1Overs.setVisibility(View.VISIBLE);
                tvTeam2Score.setVisibility(View.VISIBLE);
                tvTeam2Overs.setVisibility(View.VISIBLE);
                
                tvVs.setVisibility(View.GONE);
                tvMatchInfo.setVisibility(View.GONE);
                
                // Set scores (mock data for now)
                tvTeam1Score.setText("287-8");
                tvTeam1Overs.setText("(50.0)");
                tvTeam2Score.setText("245-10");
                tvTeam2Overs.setText("(46.2)");
            }
        }
    }
}