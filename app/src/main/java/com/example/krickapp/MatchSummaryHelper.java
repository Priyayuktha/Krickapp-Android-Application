package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Example helper class to demonstrate launching the Match Summary page
 * You can call these methods from any activity in your app
 */
public class MatchSummaryHelper {

    /**
     * Launch Match Summary with basic intent data
     */
    public static void launchMatchSummary(AppCompatActivity activity, 
                                         String team1Name, 
                                         String team2Name,
                                         String matchResult,
                                         String playerOfMatch) {
        Intent intent = new Intent(activity, MatchSummaryActivity.class);
        intent.putExtra("team1Name", team1Name);
        intent.putExtra("team2Name", team2Name);
        intent.putExtra("matchResult", matchResult);
        intent.putExtra("playerOfMatch", playerOfMatch);
        activity.startActivity(intent);
    }

    /**
     * Launch Match Summary with full data including over details
     */
    public static void launchMatchSummaryWithData(AppCompatActivity activity, 
                                                  MatchSummaryData data) {
        // Store data in a temporary holder or pass via singleton
        MatchSummaryDataHolder.setInstance(data);
        
        Intent intent = new Intent(activity, MatchSummaryActivity.class);
        intent.putExtra("team1Name", data.getTeam1Name());
        intent.putExtra("team2Name", data.getTeam2Name());
        intent.putExtra("matchResult", data.getMatchResult());
        intent.putExtra("playerOfMatch", data.getPlayerOfMatch());
        intent.putExtra("hasFullData", true);
        activity.startActivity(intent);
    }

    /**
     * Create sample match summary data for testing
     */
    public static MatchSummaryData createSampleData() {
        MatchSummaryData data = new MatchSummaryData(
            "match001",
            "Team 1",
            "Team 2",
            "Team 1 won by 14 runs",
            "Tim David"
        );

        // Over 1: Bowler D to Athena - 15 runs
        MatchSummaryData.OverDetail over1 = new MatchSummaryData.OverDetail(
            1, "D", "Athena", 15
        );
        over1.addBall(new MatchSummaryData.Ball(6, false, true, false));   // 6 - Six
        over1.addBall(new MatchSummaryData.Ball(4, false, false, true));   // 4 - Four
        over1.addBall(new MatchSummaryData.Ball(1, false, false, false));  // 1
        over1.addBall(new MatchSummaryData.Ball(1, false, false, false));  // 1
        over1.addBall(new MatchSummaryData.Ball(1, false, false, false));  // 1
        over1.addBall(new MatchSummaryData.Ball(2, false, false, false));  // 2
        data.addOverDetail(over1);

        // Over 2: Bowler D to Keon - 18 runs
        MatchSummaryData.OverDetail over2 = new MatchSummaryData.OverDetail(
            2, "D", "Keon", 18
        );
        over2.addBall(new MatchSummaryData.Ball(4, false, false, true));   // 4 - Four
        over2.addBall(new MatchSummaryData.Ball(1, false, false, false));  // 1
        over2.addBall(new MatchSummaryData.Ball(0, true, false, false));   // W - Wicket
        over2.addBall(new MatchSummaryData.Ball(1, false, false, false));  // 1
        over2.addBall(new MatchSummaryData.Ball(1, false, false, false));  // 1
        over2.addBall(new MatchSummaryData.Ball(2, false, false, false));  // 2
        data.addOverDetail(over2);

        return data;
    }

    /**
     * Example usage - Call this from a button click or after match completion
     */
    public static void exampleUsage(AppCompatActivity activity) {
        // Option 1: Simple launch with basic data
        launchMatchSummary(
            activity,
            "Warriors",
            "Knights",
            "Warriors won by 25 runs",
            "John Smith"
        );

        // Option 2: Launch with full data including overs
        // MatchSummaryData data = createSampleData();
        // launchMatchSummaryWithData(activity, data);
    }

    /**
     * Singleton holder to pass complex data between activities
     */
    public static class MatchSummaryDataHolder {
        private static MatchSummaryData instance;

        public static MatchSummaryData getInstance() {
            return instance;
        }

        public static void setInstance(MatchSummaryData data) {
            instance = data;
        }

        public static void clearInstance() {
            instance = null;
        }
    }
}
