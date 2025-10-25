package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Stack;

public class scoredesk extends AppCompatActivity {

    private TextView tvScore, tvOvers, tvBatsman1, tvBatsman1Stat, tvBatsman2, tvBatsman2Stat, tvBowler, tvBowlerStat;
    private BottomNavigationView bottomNav;

    // Match State
    private int totalRuns = 0, totalWickets = 0, totalBalls = 0, maxOvers = 10;
    private int batsman1Runs = 0, batsman1Balls = 0;
    private int batsman2Runs = 0, batsman2Balls = 0;
    private int bowlerBalls = 0, bowlerRuns = 0, bowlerWickets = 0;

    // Match data
    private String matchId = "";
    private String team1Name = "";
    private String team2Name = "";
    private String tossWinner = "";
    private String tossDecision = "";

    // Firebase
    private DatabaseReference mDatabase;

    // To manage undo
    private Stack<Action> actionStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoredesk);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get match data from intent
        Intent intent = getIntent();
        matchId = intent.getStringExtra("matchId");
        team1Name = intent.getStringExtra("team1");
        team2Name = intent.getStringExtra("team2");
        tossWinner = intent.getStringExtra("tossWinner");
        tossDecision = intent.getStringExtra("tossDecision");

        if (matchId == null || matchId.isEmpty()) {
            Toast.makeText(this, "Error: No match ID provided", Toast.LENGTH_SHORT).show();
            // Don't finish - allow manual scoring for testing
        } else {
            loadMatchData();
        }

        // Bind views
        tvScore = findViewById(R.id.tvScore);
        tvOvers = findViewById(R.id.tvOvers);
        tvBatsman1 = findViewById(R.id.tvBatsman1);
        tvBatsman1Stat = findViewById(R.id.tvBatsman1Stat);
        tvBatsman2 = findViewById(R.id.tvBatsman2);
        tvBatsman2Stat = findViewById(R.id.tvBatsman2Stat);
        tvBowler = findViewById(R.id.tvBowler);
        tvBowlerStat = findViewById(R.id.tvBowlerStat);
        bottomNav = findViewById(R.id.bottom_nav);

        // Keypad buttons
        setRunButton(R.id.btn0, 0);
        setRunButton(R.id.btn1, 1);
        setRunButton(R.id.btn2, 2);
        setRunButton(R.id.btn3, 3);
        setRunButton(R.id.btn4, 4);
        setRunButton(R.id.btn6, 6);

        findViewById(R.id.btn57).setOnClickListener(v -> addRuns(5, true)); // treat 5,7 same as 5
        findViewById(R.id.btnWD).setOnClickListener(v -> addExtra("WD"));
        findViewById(R.id.btnNB).setOnClickListener(v -> addExtra("NB"));
        findViewById(R.id.btnLB).setOnClickListener(v -> addExtra("LB"));
        findViewById(R.id.btnOUT).setOnClickListener(v -> addWicket());
        findViewById(R.id.btnUndo).setOnClickListener(v -> undoLastAction());

        findViewById(R.id.btnEndMatch).setOnClickListener(v -> {
            // Navigate to match result
            Intent resultIntent = new Intent(scoredesk.this, matchresult.class);
            resultIntent.putExtra("matchId", matchId);
            resultIntent.putExtra("team1", team1Name);
            resultIntent.putExtra("team2", team2Name);
            resultIntent.putExtra("tossWinner", tossWinner);
            resultIntent.putExtra("tossDecision", tossDecision);
            resultIntent.putExtra("totalRuns", totalRuns);
            resultIntent.putExtra("totalWickets", totalWickets);
            resultIntent.putExtra("totalOvers", (totalBalls / 6) + "." + (totalBalls % 6));
            startActivity(resultIntent);
            finish();
        });

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent homeIntent = new Intent(this, DashboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                Intent matchesIntent = new Intent(this, MatchesListActivity.class);
                matchesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(matchesIntent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(this, create_match.class));
                return true;
            } else if (itemId == R.id.navigation_live) {
                Intent liveIntent = new Intent(this, MatchesListActivity.class);
                liveIntent.putExtra("filterStatus", "live");
                liveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(liveIntent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(this, MoreActivity.class));
                return true;
            }
            return false;
        });

        updateUI();
    }

    // Handle normal runs
    private void setRunButton(int id, int runs) {
        Button btn = findViewById(id);
        btn.setOnClickListener(v -> addRuns(runs, true));
    }

    private void addRuns(int runs, boolean legal) {
        totalRuns += runs;
        batsman1Runs += runs;
        bowlerRuns += runs;
        if (legal) {
            totalBalls++;
            bowlerBalls++;
            batsman1Balls++; // Only increment for legal balls
        }
        actionStack.push(new Action(runs, false, legal));
        updateUI();
    }

    // Handle extras (wide, no-ball, leg-bye)
    private void addExtra(String type) {
        totalRuns++;
        bowlerRuns++;
        actionStack.push(new Action(1, false, false));
        Toast.makeText(this, type + " given", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    // Handle wicket
    private void addWicket() {
        totalWickets++;
        bowlerWickets++;
        batsman1Balls++;
        totalBalls++;
        bowlerBalls++;
        actionStack.push(new Action(0, true, true));
        Toast.makeText(this, "Wicket!", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    // Undo last action
    private void undoLastAction() {
        if (!actionStack.isEmpty()) {
            Action last = actionStack.pop();
            totalRuns -= last.runs;
            if (last.wicket) {
                totalWickets--;
                bowlerWickets--;
            }
            if (last.legalBall) {
                totalBalls--;
                bowlerBalls--;
                batsman1Balls--; // Only decrement once for legal balls
            }
            bowlerRuns -= last.runs;
            batsman1Runs -= last.runs;
            updateUI();
        } else {
            Toast.makeText(this, "Nothing to undo", Toast.LENGTH_SHORT).show();
        }
    }

    // Refresh display
    private void updateUI() {
        tvScore.setText(totalRuns + "/" + totalWickets);
        tvOvers.setText("(" + (totalBalls / 6) + "." + (totalBalls % 6) + "/" + maxOvers + ")");
        tvBatsman1Stat.setText(batsman1Runs + "(" + batsman1Balls + ")");
        tvBatsman2Stat.setText(batsman2Runs + "(" + batsman2Balls + ")");
        tvBowlerStat.setText(bowlerBalls + " " + bowlerWickets + " " + bowlerRuns);
        
        // Save live score updates to Firebase if matchId exists
        if (matchId != null && !matchId.isEmpty()) {
            saveLiveScoreToFirebase();
        }
    }
    
    /**
     * Save live score updates to Firebase for real-time tracking
     */
    private void saveLiveScoreToFirebase() {
        java.util.HashMap<String, Object> scoreUpdate = new java.util.HashMap<>();
        scoreUpdate.put("totalRuns", totalRuns);
        scoreUpdate.put("totalWickets", totalWickets);
        scoreUpdate.put("totalBalls", totalBalls);
        scoreUpdate.put("overs", (totalBalls / 6) + "." + (totalBalls % 6));
        scoreUpdate.put("batsman1Runs", batsman1Runs);
        scoreUpdate.put("batsman1Balls", batsman1Balls);
        scoreUpdate.put("batsman2Runs", batsman2Runs);
        scoreUpdate.put("batsman2Balls", batsman2Balls);
        scoreUpdate.put("bowlerRuns", bowlerRuns);
        scoreUpdate.put("bowlerWickets", bowlerWickets);
        scoreUpdate.put("bowlerBalls", bowlerBalls);
        scoreUpdate.put("lastUpdated", System.currentTimeMillis());
        
        // Update match status to live if not already
        mDatabase.child("matches").child(matchId).child("status").setValue("live");
        
        // Save live score data
        mDatabase.child("matches").child(matchId).child("liveScore").updateChildren(scoreUpdate)
            .addOnFailureListener(e -> {
                // Silently handle failure - don't interrupt scoring
                android.util.Log.e("scoredesk", "Failed to save live score: " + e.getMessage());
            });
    }

    /**
     * Load match data from Firebase
     */
    private void loadMatchData() {
        mDatabase.child("matches").child(matchId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Match match = snapshot.getValue(Match.class);
                    if (match != null) {
                        if (match.getTeam1() != null) {
                            team1Name = match.getTeam1().getTeamName();
                        }
                        if (match.getTeam2() != null) {
                            team2Name = match.getTeam2().getTeamName();
                        }
                        tossWinner = match.getTossWinner();
                        tossDecision = match.getTossDecision();
                        
                        // Update team names in UI if needed
                        if (tvBatsman1 != null && team1Name != null) {
                            tvBatsman1.setText("Batsman 1");
                        }
                        if (tvBatsman2 != null && team2Name != null) {
                            tvBatsman2.setText("Batsman 2");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(scoredesk.this, "Error loading match: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Action model for undo
    private static class Action {
        int runs;
        boolean wicket;
        boolean legalBall;

        Action(int runs, boolean wicket, boolean legalBall) {
            this.runs = runs;
            this.wicket = wicket;
            this.legalBall = legalBall;
        }
    }
}
