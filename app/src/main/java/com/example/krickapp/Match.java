package com.example.krickapp;

import java.util.Map;

public class Match {
    // Match Info
    private String matchName;
    private String venue;
    private String date;
    private String time;
    private String matchType;
    private String createdBy;
    private String status = "scheduled"; // Default status

    // Team Data
    private Team team1;
    private Team team2;

    // Inside Match.java (next to other fields and setters)

    // Field (already present if you used my original Match.java, but ensure it is public or has a setter)
    private String matchId;

// ... (rest of the getters and setters)

    // Add this setter:
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    // Ensure you have the getter too:
    public String getMatchId() {
        return matchId;
    }

    // Required empty constructor for Firebase DataSnapshot.getValue(Match.class)
    public Match() {
    }

    // Constructor from create_match.java data
    public Match(String matchName, String venue, String date, String time, String matchType) {
        this.matchName = matchName;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.matchType = matchType;
    }

    // Nested class for Team Details (Firebase needs this for structure)
    public static class Team {
        private String teamName;
        private Map<String, String> players;

        public Team() {}

        public Team(String teamName, Map<String, String> players) {
            this.teamName = teamName;
            this.players = players;
        }

        // Getters
        public String getTeamName() { return teamName; }
        public Map<String, String> getPlayers() { return players; }

        // Setters
        public void setTeamName(String teamName) { this.teamName = teamName; }
        public void setPlayers(Map<String, String> players) { this.players = players; }
    }


    // GETTERS (Required for Firebase to read data correctly)

    public String getMatchName() { return matchName; }
    public String getVenue() { return venue; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getMatchType() { return matchType; }
    public String getCreatedBy() { return createdBy; }
    public String getStatus() { return status; }
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }


    // SETTERS

    public void setMatchName(String matchName) { this.matchName = matchName; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setMatchType(String matchType) { this.matchType = matchType; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setStatus(String status) { this.status = status; }
    public void setTeam1(Team team1) { this.team1 = team1; }
    public void setTeam2(Team team2) { this.team2 = team2; }
}