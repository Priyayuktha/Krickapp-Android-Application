package com.example.krickapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class tossresult extends AppCompatActivity {

    Button btnTeamA, btnTeamB, btnBat, btnBowl, btnSave;
    BottomNavigationView bottomNav;
    FloatingActionButton fab;

    String tossWinner = "";
    String tossDecision = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tossresult);

        // find views
        btnTeamA = findViewById(R.id.btnTeamA);
        btnTeamB = findViewById(R.id.btnTeamB);
        btnBat = findViewById(R.id.btnBat);
        btnBowl = findViewById(R.id.btnBowl);
        btnSave = findViewById(R.id.btnSave);
        bottomNav = findViewById(R.id.bottom_nav);
        fab = findViewById(R.id.fab_create);

        // --- TEAM SELECTION ---
        btnTeamA.setOnClickListener(v -> {
            tossWinner = "Team A (TS)";
            highlightSelection(btnTeamA, btnTeamB);
            Toast.makeText(this, "Toss Winner: " + tossWinner, Toast.LENGTH_SHORT).show();
        });

        btnTeamB.setOnClickListener(v -> {
            tossWinner = "Team B (SB)";
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
                String result = tossWinner + " won the toss and chose to " + tossDecision;
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                // 👉 later you can save to database or pass via Intent
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
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });

        fab.setOnClickListener(v ->
                Toast.makeText(this, "Create New Match", Toast.LENGTH_SHORT).show()
        );
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
}
