package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class matchinfo extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private DatabaseReference mDatabase;
    private String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchinfo);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("matches");
        
        // Get match ID from intent
        matchId = getIntent().getStringExtra("matchId");
        
        // Back button
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setSelectedItemId(R.id.navigation_matches);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                startActivity(new Intent(this, MatchesListActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(this, create_match.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_live) {
                Toast.makeText(this, "Live matches", Toast.LENGTH_SHORT).show();
                return false;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(this, MoreActivity.class));
                finish();
                return true;
            }
            return false;
        });

        // Load match data from Firebase
        if (matchId != null && !matchId.isEmpty()) {
            loadMatchData();
        } else {
            Toast.makeText(this, "No match data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMatchData() {
        mDatabase.child(matchId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Match match = snapshot.getValue(Match.class);
                    if (match != null) {
                        displayMatchData(match);
                    } else {
                        Toast.makeText(matchinfo.this, "Failed to load match data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(matchinfo.this, "Match not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(matchinfo.this, 
                    "Error loading match: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMatchData(Match match) {
        // Setup rows with actual match data
        String matchType = match.getMatchType() != null ? match.getMatchType() : "Not specified";
        String date = match.getDate() != null ? match.getDate() : "TBD";
        String time = match.getTime() != null ? match.getTime() : "TBD";
        String venue = match.getVenue() != null ? match.getVenue() : "Not specified";
        
        // Get team names for display
        String team1Name = "Team 1";
        String team2Name = "Team 2";
        
        if (match.getTeam1() != null && match.getTeam1().getTeamName() != null) {
            team1Name = match.getTeam1().getTeamName();
        }
        
        if (match.getTeam2() != null && match.getTeam2().getTeamName() != null) {
            team2Name = match.getTeam2().getTeamName();
        }
        
        // Setup info rows
        setupRow(R.id.rowMatchType, R.drawable.matchtype, "Match Type", matchType);
        setupRow(R.id.rowDate, R.drawable.date, "Date", date);
        setupRow(R.id.rowTime, R.drawable.time, "Time", time);
        setupRow(R.id.rowVenue, R.drawable.venue, "Venue", venue);
        setupRow(R.id.rowCity, R.drawable.city, "City", "Not specified");
        setupRow(R.id.rowToss, R.drawable.toos, "Toss Win", "Not yet decided");
        setupRow(R.id.rowHost, R.drawable.host, "Host", "Local Cricket Association");
    }

    private void setupRow(int rowId, int iconRes, String label, String value) {
        try {
            LinearLayout row = findViewById(rowId);
            if (row != null) {
                ImageView icon = row.findViewById(R.id.rowIcon);
                TextView lbl = row.findViewById(R.id.rowLabel);
                TextView val = row.findViewById(R.id.rowValue);

                if (icon != null) icon.setImageResource(iconRes);
                if (lbl != null) lbl.setText(label);
                if (val != null) val.setText(value);
            }
        } catch (Exception e) {
            // Silently handle missing views
            e.printStackTrace();
        }
    }
}