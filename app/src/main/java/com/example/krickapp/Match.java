package com.example.krickapp;

import java.util.Map;

public class Match {
    private String matchId;
    private String matchName;
    private String venue;
    private String date;
    private String time;
    private String matchType;
    private String status;
    private String createdBy;
    private long createdAt;
    private Team team1;
    private Team team2;
    
    // Required empty constructor for Firebase
    public Match() {}
    
    public Match(String matchName, String venue, String date, String time, String matchType) {
        this.matchName = matchName;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.matchType = matchType;
        this.status = "scheduled";
        this.createdAt = System.currentTimeMillis();
    }
    
    // Getters and setters
    public String getMatchId() { 
        return matchId; 
    }
    
    public void setMatchId(String matchId) { 
        this.matchId = matchId; 
    }
    
    public String getMatchName() { 
        return matchName; 
    }
    
    public void setMatchName(String matchName) { 
        this.matchName = matchName; 
    }
    
    public String getVenue() { 
        return venue; 
    }
    
    public void setVenue(String venue) { 
        this.venue = venue; 
    }
    
    public String getDate() { 
        return date; 
    }
    
    public void setDate(String date) { 
        this.date = date; 
    }
    
    public String getTime() { 
        return time; 
    }
    
    public void setTime(String time) { 
        this.time = time; 
    }
    
    public String getMatchType() { 
        return matchType; 
    }
    
    public void setMatchType(String matchType) { 
        this.matchType = matchType; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getCreatedBy() { 
        return createdBy; 
    }
    
    public void setCreatedBy(String createdBy) { 
        this.createdBy = createdBy; 
    }
    
    public long getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(long createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public Team getTeam1() { 
        return team1; 
    }
    
    public void setTeam1(Team team1) { 
        this.team1 = team1; 
    }
    
    public Team getTeam2() { 
        return team2; 
    }
    
    public void setTeam2(Team team2) { 
        this.team2 = team2; 
    }
    
    // Nested Team class
    public static class Team {
        private String name;
        private Map<String, String> players;
        
        public Team() {}
        
        public Team(String name, Map<String, String> players) {
            this.name = name;
            this.players = players;
        }
        
        public String getName() { 
            return name; 
        }
        
        public void setName(String name) { 
            this.name = name; 
        }
        
        public Map<String, String> getPlayers() { 
            return players; 
        }
        
        public void setPlayers(Map<String, String> players) { 
            this.players = players; 
        }
    }
}
