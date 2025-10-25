package com.example.krickapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class tossresult extends AppCompatActivity {

    Button btnTeamA, btnTeamB, btnBat, btnBowl, btnSave;
    BottomNavigationView bottomNav;

    String tossWinner = "";
    String tossDecision = "";
    String matchId = "";
    String team1Name = "";
    String team2Name = "";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tossresult);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get match data from intent
        Intent intent = getIntent();
        matchId = intent.getStringExtra("matchId");
        team1Name = intent.getStringExtra("team1");
        team2Name = intent.getStringExtra("team2");

        if (matchId == null || matchId.isEmpty()) {
            Toast.makeText(this, "Error: No match ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // find views
        btnTeamA = findViewById(R.id.btnTeamA);
        btnTeamB = findViewById(R.id.btnTeamB);
        btnBat = findViewById(R.id.btnBat);
        btnBowl = findViewById(R.id.btnBowl);
        btnSave = findViewById(R.id.btnSave);
        bottomNav = findViewById(R.id.bottom_nav);

        // Set team names on buttons if available
        if (team1Name != null && !team1Name.isEmpty()) {
            btnTeamA.setText(team1Name);
        }
        if (team2Name != null && !team2Name.isEmpty()) {
            btnTeamB.setText(team2Name);
        }

        // --- TEAM SELECTION ---
        btnTeamA.setOnClickListener(v -> {
            tossWinner = team1Name != null && !team1Name.isEmpty() ? team1Name : "Team A";
            highlightSelection(btnTeamA, btnTeamB);
            Toast.makeText(this, "Toss Winner: " + tossWinner, Toast.LENGTH_SHORT).show();
        });

        btnTeamB.setOnClickListener(v -> {
            tossWinner = team2Name != null && !team2Name.isEmpty() ? team2Name : "Team B";
            highlightSelection(btnTeamB, btnTeamA);
            Toast.makeText(this, "Toss Winner: " + tossWinner, Toast.LENGTH_SHORT).show();
        });

        // --- DECISION SELECTION ---
        btnBat.setOnClickListener(v -> {
            tossDecision = "Bat";
            highlightSelection(btnBat, btnBowl);
            Toast.makeText(this, "Opted to: " + tossDecision, Toast.LENGTH_SHORT).show();
        });

        btnBowl.setOnClickListener(v -> {
            tossDecision = "Bowl";
            highlightSelection(btnBowl, btnBat);
            Toast.makeText(this, "Opted to: " + tossDecision, Toast.LENGTH_SHORT).show();
        });

        // --- SAVE BUTTON ---
        btnSave.setOnClickListener(v -> {
            if (tossWinner.isEmpty() || tossDecision.isEmpty()) {
                Toast.makeText(this, "Please select toss winner and decision!", Toast.LENGTH_SHORT).show();
            } else {
                saveTossAndStartMatch();
            }
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(tossresult.this, DashboardActivity.class));
                return true;
            } else if (id == R.id.navigation_matches) {
                startActivity(new Intent(tossresult.this, MatchesListActivity.class));
                return true;
            } else if (id == R.id.navigation_create) {
                startActivity(new Intent(tossresult.this, create_match.class));
                return true;
            } else if (id == R.id.navigation_live) {
                Toast.makeText(this, "Live Scoring - Coming Soon", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.navigation_more) {
                startActivity(new Intent(tossresult.this, MoreActivity.class));
                return true;
            }

            return false;
        });
    }


    /**
     * Utility method to highlight the selected button and reset the other
     */
    private void highlightSelection(Button selected, Button other) {
        // Selected button: dark background
        selected.setAlpha(1.0f);
        // Unselected button: light transparent look
        other.setAlpha(0.5f);
    }

    /**
     * Save toss result to Firebase and update match status to "live"
     */
    private void saveTossAndStartMatch() {
        String result = tossWinner + " won the toss and chose to " + tossDecision;
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        // Update match in Firebase
        Map<String, Object> updates = new HashMap<>();
        updates.put("tossWinner", tossWinner);
        updates.put("tossDecision", tossDecision);
        updates.put("status", "live");

        mDatabase.child("matches").child(matchId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(tossresult.this, "Toss saved! Match is now live.", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to score desk to start scoring
                    Intent scoreDeskIntent = new Intent(tossresult.this, scoredesk.class);
                    scoreDeskIntent.putExtra("matchId", matchId);
                    scoreDeskIntent.putExtra("team1", team1Name);
                    scoreDeskIntent.putExtra("team2", team2Name);
                    scoreDeskIntent.putExtra("tossWinner", tossWinner);
                    scoreDeskIntent.putExtra("tossDecision", tossDecision);
                    startActivity(scoreDeskIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(tossresult.this, "Error saving toss: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
