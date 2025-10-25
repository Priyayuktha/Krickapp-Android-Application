package com.example.krickapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
        holder.bind(match, context, this);
        
        // Add click listener to open appropriate screen based on match status
        holder.itemView.setOnClickListener(v -> {
            String status = match.getStatus() != null ? match.getStatus().toLowerCase() : "scheduled";
            
            if (status.equals("completed")) {
                // Open match summary for completed matches
                if (context instanceof AppCompatActivity) {
                    MatchSummaryHelper.launchMatchSummaryFromFirebase(
                        (AppCompatActivity) context, 
                        match.getMatchId()
                    );
                }
            } else if (status.equals("ongoing") || status.equals("live")) {
                // Navigate to score desk for live matches
                Intent intent = new Intent(context, scoredesk.class);
                intent.putExtra("matchId", match.getMatchId());
                context.startActivity(intent);
            } else {
                // For scheduled matches, open match info with option to start
                Intent intent = new Intent(context, matchinfo.class);
                intent.putExtra("matchId", match.getMatchId());
                context.startActivity(intent);
            }
        });
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

        public void bind(Match match, Context context, MatchesAdapter adapter) {
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
                tvMatchInfo.setText(adapter.calculateTimeUntilMatch(match.getDate(), match.getTime()));
                
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
                
                // Try to get live score from match data, fallback to mock data
                String team1Score = "0-0";
                String team1Overs = "(0.0)";
                String team2Score = "0-0";
                String team2Overs = "(0.0)";
                
                // TODO: Implement live score retrieval from Firebase in real-time
                // For now, show placeholder
                tvTeam1Score.setText(team1Score);
                tvTeam1Overs.setText(team1Overs);
                tvTeam2Score.setText(team2Score);
                tvTeam2Overs.setText(team2Overs);
                
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
                
                // Try to get final scores from match summary
                // TODO: Implement final score retrieval from Firebase match summary
                // For now show placeholder
                tvTeam1Score.setText("0-0");
                tvTeam1Overs.setText("(0.0)");
                tvTeam2Score.setText("0-0");
                tvTeam2Overs.setText("(0.0)");
            }
        }
    }
    
    /**
     * Calculate time remaining until match starts
     * @param dateStr Date in format "dd/MM/yyyy" or similar
     * @param timeStr Time in format "HH:mm" or similar
     * @return Formatted string like "Starts in 2h 30m" or "Starts tomorrow"
     */
    private String calculateTimeUntilMatch(String dateStr, String timeStr) {
        if (dateStr == null || dateStr.equals("TBD") || timeStr == null || timeStr.equals("TBD")) {
            return "Start time TBD";
        }
        
        try {
            // Parse the date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateTimeStr = dateStr + " " + timeStr;
            Date matchDate = dateFormat.parse(dateTimeStr);
            
            if (matchDate == null) {
                return "Start time TBD";
            }
            
            // Calculate difference
            long currentTime = System.currentTimeMillis();
            long matchTime = matchDate.getTime();
            long diffMillis = matchTime - currentTime;
            
            // If match is in the past
            if (diffMillis < 0) {
                return "Match time passed";
            }
            
            // Convert to hours and minutes
            long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) - TimeUnit.HOURS.toMinutes(hours);
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis);
            
            // Format the output
            if (days > 0) {
                if (days == 1) {
                    return "Starts tomorrow";
                } else if (days < 7) {
                    return "Starts in " + days + " days";
                } else {
                    return "Starts on " + dateStr;
                }
            } else if (hours > 0) {
                if (minutes > 0) {
                    return "Starts in " + hours + "h " + minutes + "m";
                } else {
                    return "Starts in " + hours + "h";
                }
            } else if (minutes > 0) {
                return "Starts in " + minutes + "m";
            } else {
                return "Starting soon";
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
            // Try alternative date format
            try {
                SimpleDateFormat altFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String dateTimeStr = dateStr + " " + timeStr;
                Date matchDate = altFormat.parse(dateTimeStr);
                
                if (matchDate != null) {
                    long diffMillis = matchDate.getTime() - System.currentTimeMillis();
                    if (diffMillis < 0) return "Match time passed";
                    
                    long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) - TimeUnit.HOURS.toMinutes(hours);
                    
                    if (hours > 24) {
                        return "Starts on " + dateStr;
                    } else if (hours > 0) {
                        return "Starts in " + hours + "h " + minutes + "m";
                    } else {
                        return "Starts in " + minutes + "m";
                    }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return "Start time TBD";
        }
    }
}