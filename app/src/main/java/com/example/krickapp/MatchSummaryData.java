package com.example.krickapp;

import java.util.ArrayList;
import java.util.List;

public class MatchSummaryData {
    private String matchId;
    private String team1Name;
    private String team2Name;
    private String matchResult;
    private String playerOfMatch;
    private List<OverDetail> overDetails;

    public MatchSummaryData() {
        this.overDetails = new ArrayList<>();
    }

    public MatchSummaryData(String matchId, String team1Name, String team2Name, 
                           String matchResult, String playerOfMatch) {
        this.matchId = matchId;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.matchResult = matchResult;
        this.playerOfMatch = playerOfMatch;
        this.overDetails = new ArrayList<>();
    }

    // Getters and Setters
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getPlayerOfMatch() {
        return playerOfMatch;
    }

    public void setPlayerOfMatch(String playerOfMatch) {
        this.playerOfMatch = playerOfMatch;
    }

    public List<OverDetail> getOverDetails() {
        return overDetails;
    }

    public void setOverDetails(List<OverDetail> overDetails) {
        this.overDetails = overDetails;
    }

    public void addOverDetail(OverDetail overDetail) {
        this.overDetails.add(overDetail);
    }

    // Inner class for Over Details
    public static class OverDetail {
        private int overNumber;
        private String bowlerName;
        private String batsmanName;
        private int totalRuns;
        private List<Ball> balls;

        public OverDetail() {
            this.balls = new ArrayList<>();
        }

        public OverDetail(int overNumber, String bowlerName, String batsmanName, int totalRuns) {
            this.overNumber = overNumber;
            this.bowlerName = bowlerName;
            this.batsmanName = batsmanName;
            this.totalRuns = totalRuns;
            this.balls = new ArrayList<>();
        }

        public int getOverNumber() {
            return overNumber;
        }

        public void setOverNumber(int overNumber) {
            this.overNumber = overNumber;
        }

        public String getBowlerName() {
            return bowlerName;
        }

        public void setBowlerName(String bowlerName) {
            this.bowlerName = bowlerName;
        }

        public String getBatsmanName() {
            return batsmanName;
        }

        public void setBatsmanName(String batsmanName) {
            this.batsmanName = batsmanName;
        }

        public int getTotalRuns() {
            return totalRuns;
        }

        public void setTotalRuns(int totalRuns) {
            this.totalRuns = totalRuns;
        }

        public List<Ball> getBalls() {
            return balls;
        }

        public void setBalls(List<Ball> balls) {
            this.balls = balls;
        }

        public void addBall(Ball ball) {
            this.balls.add(ball);
        }

        public String getOverText() {
            return overNumber + ". Bowler " + bowlerName + " to " + batsmanName + "... " + totalRuns + " runs";
        }
    }

    // Inner class for Ball
    public static class Ball {
        private int runs;
        private boolean isWicket;
        private boolean isSix;
        private boolean isFour;

        public Ball() {
        }

        public Ball(int runs, boolean isWicket, boolean isSix, boolean isFour) {
            this.runs = runs;
            this.isWicket = isWicket;
            this.isSix = isSix;
            this.isFour = isFour;
        }

        public int getRuns() {
            return runs;
        }

        public void setRuns(int runs) {
            this.runs = runs;
        }

        public boolean isWicket() {
            return isWicket;
        }

        public void setWicket(boolean wicket) {
            isWicket = wicket;
        }

        public boolean isSix() {
            return isSix;
        }

        public void setSix(boolean six) {
            isSix = six;
        }

        public boolean isFour() {
            return isFour;
        }

        public void setFour(boolean four) {
            isFour = four;
        }
    }
}
