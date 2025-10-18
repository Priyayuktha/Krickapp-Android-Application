package com.example.krickapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class reset_password extends AppCompatActivity {

    EditText teamName, player1, player2, player3, player4, player5, player6, player7, player8, player9, player10, player11;
    Button saveBtn, btnTeam1, btnTeam2;
    TextView pageTitle;
    int currentTeam = 1; // Track which team is currently being edited
    
    // Store team data
    String team1Name = "", team2Name = "";
    String[] team1Players = new String[11];
    String[] team2Players = new String[11];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        // Initialize views
        teamName = findViewById(R.id.etTeamName);
        player1 = findViewById(R.id.etPlayer1);
        player2 = findViewById(R.id.etPlayer2);
        player3 = findViewById(R.id.etPlayer3);
        player4 = findViewById(R.id.etPlayer4);
        player5 = findViewById(R.id.etPlayer5);
        player6 = findViewById(R.id.etPlayer6);
        player7 = findViewById(R.id.etPlayer7);
        player8 = findViewById(R.id.etPlayer8);
        player9 = findViewById(R.id.etPlayer9);
        player10 = findViewById(R.id.etPlayer10);
        player11 = findViewById(R.id.etPlayer11);
        
        saveBtn = findViewById(R.id.btnSave);
        btnTeam1 = findViewById(R.id.btnTeam1);
        btnTeam2 = findViewById(R.id.btnTeam2);
        pageTitle = findViewById(R.id.pageTitle);

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
        saveBtn.setOnClickListener(v -> {
            saveCurrentTeamData();
            
            if (validateAllData()) {
                Toast.makeText(reset_password.this, 
                    "Both teams saved successfully!", 
                    Toast.LENGTH_LONG).show();
                // TODO: Save to Firebase and navigate to next screen
                finish();
            } else {
                Toast.makeText(reset_password.this, 
                    "Please complete both team details", 
                    Toast.LENGTH_SHORT).show();
            }
        });
        
        // Initialize button colors
        updateButtonColors();
    }
    
    private void saveCurrentTeamData() {
        if (currentTeam == 1) {
            team1Name = teamName.getText().toString().trim();
            team1Players[0] = player1.getText().toString().trim();
            team1Players[1] = player2.getText().toString().trim();
            team1Players[2] = player3.getText().toString().trim();
            team1Players[3] = player4.getText().toString().trim();
            team1Players[4] = player5.getText().toString().trim();
            team1Players[5] = player6.getText().toString().trim();
            team1Players[6] = player7.getText().toString().trim();
            team1Players[7] = player8.getText().toString().trim();
            team1Players[8] = player9.getText().toString().trim();
            team1Players[9] = player10.getText().toString().trim();
            team1Players[10] = player11.getText().toString().trim();
        } else {
            team2Name = teamName.getText().toString().trim();
            team2Players[0] = player1.getText().toString().trim();
            team2Players[1] = player2.getText().toString().trim();
            team2Players[2] = player3.getText().toString().trim();
            team2Players[3] = player4.getText().toString().trim();
            team2Players[4] = player5.getText().toString().trim();
            team2Players[5] = player6.getText().toString().trim();
            team2Players[6] = player7.getText().toString().trim();
            team2Players[7] = player8.getText().toString().trim();
            team2Players[8] = player9.getText().toString().trim();
            team2Players[9] = player10.getText().toString().trim();
            team2Players[10] = player11.getText().toString().trim();
        }
    }
    
    private void loadTeamData() {
        if (currentTeam == 1) {
            teamName.setText(team1Name);
            player1.setText(team1Players[0] != null ? team1Players[0] : "");
            player2.setText(team1Players[1] != null ? team1Players[1] : "");
            player3.setText(team1Players[2] != null ? team1Players[2] : "");
            player4.setText(team1Players[3] != null ? team1Players[3] : "");
            player5.setText(team1Players[4] != null ? team1Players[4] : "");
            player6.setText(team1Players[5] != null ? team1Players[5] : "");
            player7.setText(team1Players[6] != null ? team1Players[6] : "");
            player8.setText(team1Players[7] != null ? team1Players[7] : "");
            player9.setText(team1Players[8] != null ? team1Players[8] : "");
            player10.setText(team1Players[9] != null ? team1Players[9] : "");
            player11.setText(team1Players[10] != null ? team1Players[10] : "");
        } else {
            teamName.setText(team2Name);
            player1.setText(team2Players[0] != null ? team2Players[0] : "");
            player2.setText(team2Players[1] != null ? team2Players[1] : "");
            player3.setText(team2Players[2] != null ? team2Players[2] : "");
            player4.setText(team2Players[3] != null ? team2Players[3] : "");
            player5.setText(team2Players[4] != null ? team2Players[4] : "");
            player6.setText(team2Players[5] != null ? team2Players[5] : "");
            player7.setText(team2Players[6] != null ? team2Players[6] : "");
            player8.setText(team2Players[7] != null ? team2Players[7] : "");
            player9.setText(team2Players[8] != null ? team2Players[8] : "");
            player10.setText(team2Players[9] != null ? team2Players[9] : "");
            player11.setText(team2Players[10] != null ? team2Players[10] : "");
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
        // Check if both teams have at least team name and 11 players
        boolean team1Valid = !team1Name.isEmpty() && 
            team1Players[0] != null && !team1Players[0].isEmpty() &&
            team1Players[10] != null && !team1Players[10].isEmpty();
            
        boolean team2Valid = !team2Name.isEmpty() && 
            team2Players[0] != null && !team2Players[0].isEmpty() &&
            team2Players[10] != null && !team2Players[10].isEmpty();
            
        return team1Valid && team2Valid;
    }
}
