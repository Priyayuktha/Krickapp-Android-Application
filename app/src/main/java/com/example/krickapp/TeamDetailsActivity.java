package com.example.krickapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView; // New Import
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TeamDetailsActivity extends AppCompatActivity {

    private EditText teamName;
    private final EditText[] playerEditTexts = new EditText[11];
    private Button saveBtn, btnTeam1, btnTeam2;
    private TextView pageTitle;
    private BottomNavigationView bottomNav; // ADDED: Field for BottomNavigationView
    private int currentTeam = 1; // Track which team is currently being edited

    // Store team data
    private String team1Name = "", team2Name = "";
    private final String[] team1Players = new String[11];
    private final String[] team2Players = new String[11];

    // Firebase
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;

    // Match data from previous activity
    private String matchName, venue, date, time, matchType;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving match...");
        progressDialog.setCancelable(false);

        // Get match data from previous activity
        Intent intent = getIntent();
        matchName = intent.getStringExtra("matchName");
        venue = intent.getStringExtra("venue");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        matchType = intent.getStringExtra("matchType");

        // Initialize views
        teamName = findViewById(R.id.etTeamName);
        playerEditTexts[0] = findViewById(R.id.etPlayer1);
        playerEditTexts[1] = findViewById(R.id.etPlayer2);
        playerEditTexts[2] = findViewById(R.id.etPlayer3);
        playerEditTexts[3] = findViewById(R.id.etPlayer4);
        playerEditTexts[4] = findViewById(R.id.etPlayer5);
        playerEditTexts[5] = findViewById(R.id.etPlayer6);
        playerEditTexts[6] = findViewById(R.id.etPlayer7);
        playerEditTexts[7] = findViewById(R.id.etPlayer8);
        playerEditTexts[8] = findViewById(R.id.etPlayer9);
        playerEditTexts[9] = findViewById(R.id.etPlayer10);
        playerEditTexts[10] = findViewById(R.id.etPlayer11);

        saveBtn = findViewById(R.id.btnSave);
        btnTeam1 = findViewById(R.id.btnTeam1);
        btnTeam2 = findViewById(R.id.btnTeam2);
        pageTitle = findViewById(R.id.pageTitle);
        bottomNav = findViewById(R.id.bottom_nav); // ADDED: Initialize BottomNavigationView

        // Set initial team
        pageTitle.setText("Enter Team 1 Details");

        // Team 1 button click
        btnTeam1.setOnClickListener(v -> {
            saveCurrentTeamData();
            currentTeam = 1;
            loadTeamData();
            updateButtonColors();
            pageTitle.setText("Enter Team 1 Details");
        });

        // Team 2 button click
        btnTeam2.setOnClickListener(v -> {
            saveCurrentTeamData();
            currentTeam = 2;
            loadTeamData();
            updateButtonColors();
            pageTitle.setText("Enter Team 2 Details");
        });

        // Save button click
        saveBtn.setOnClickListener(v -> saveMatchToFirebase());

        // ADDED: Bottom Navigation Listener
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Assuming R.id.navHome is the ID for the Home icon in your bottom_nav_menu
            if (itemId == R.id.navigation_home) {
                Intent homeIntent = new Intent(TeamDetailsActivity.this, DashboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish(); // Close this activity
                return true;
            }
            // Handle other navigation items here (e.g., navschedule, navCreate, etc.)
            else if (itemId == R.id.navigation_matches) {
                startActivity(new Intent(TeamDetailsActivity.this, MatchesListActivity.class));
                return true;
            }
            // For 'Create' icon, you are already in the match creation flow, so ignore or show a Toast.

            return false;
        });

        // Initialize button colors
        updateButtonColors();
    }

    private void saveCurrentTeamData() {
        String[] players = (currentTeam == 1) ? team1Players : team2Players;
        if (currentTeam == 1) {
            team1Name = teamName.getText().toString().trim();
        } else {
            team2Name = teamName.getText().toString().trim();
        }
        for (int i = 0; i < 11; i++) {
            players[i] = playerEditTexts[i].getText().toString().trim();
        }
    }

    private void loadTeamData() {
        String name = (currentTeam == 1) ? team1Name : team2Name;
        String[] players = (currentTeam == 1) ? team1Players : team2Players;
        teamName.setText(name);
        for (int i = 0; i < 11; i++) {
            playerEditTexts[i].setText(players[i] != null ? players[i] : "");
        }
    }

    private void updateButtonColors() {
        if (currentTeam == 1) {
            btnTeam1.setBackgroundColor(Color.parseColor("#FFD600")); // Yellow/Gold
            btnTeam2.setBackgroundColor(Color.parseColor("#E0E0E0")); // Gray
        } else {
            btnTeam1.setBackgroundColor(Color.parseColor("#E0E0E0")); // Gray
            btnTeam2.setBackgroundColor(Color.parseColor("#FFD600")); // Yellow/Gold
        }
    }

    private boolean validateAllData() {
        // Check if both teams have team name and all 11 players
        if (team1Name.isEmpty()) {
            Toast.makeText(this, "Please enter Team 1 name", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < 11; i++) {
            if (team1Players[i] == null || team1Players[i].isEmpty()) {
                Toast.makeText(this, "Please enter all 11 players for Team 1", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (team2Name.isEmpty()) {
            Toast.makeText(this, "Please enter Team 2 name", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < 11; i++) {
            if (team2Players[i] == null || team2Players[i].isEmpty()) {
                Toast.makeText(this, "Please enter all 11 players for Team 2", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void saveMatchToFirebase() {
        saveCurrentTeamData();

        if (!validateAllData()) {
            Toast.makeText(this, "Please complete both team details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressDialog.show();

        // Get current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String creatorId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "unknown";

        // Create unique match ID
        String matchId = mDatabase.child("matches").push().getKey();

        if (matchId == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Failed to generate match ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create match object
        Match match = new Match(matchName, venue, date, time, matchType);
        match.setCreatedBy(creatorId);

        // Team 1 data
        Map<String, String> team1PlayersList = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            if (team1Players[i] != null && !team1Players[i].isEmpty()) {
                team1PlayersList.put("player" + (i + 1), team1Players[i]);
            }
        }
        match.setTeam1(new Match.Team(team1Name, team1PlayersList));

        // Team 2 data
        Map<String, String> team2PlayersList = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            if (team2Players[i] != null && !team2Players[i].isEmpty()) {
                team2PlayersList.put("player" + (i + 1), team2Players[i]);
            }
        }
        match.setTeam2(new Match.Team(team2Name, team2PlayersList));

        // Save to Firebase
        mDatabase.child("matches").child(matchId).setValue(match)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Match created successfully!", Toast.LENGTH_SHORT).show();
                        // Navigate back to dashboard
                        Intent intent = new Intent(TeamDetailsActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to save match: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}