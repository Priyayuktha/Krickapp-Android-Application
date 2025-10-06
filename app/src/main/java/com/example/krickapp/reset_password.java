package com.example.krickapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class reset_password extends AppCompatActivity {

    EditText teamName, player1, player2, player3, player4, player5, player6, player7, player8, player9, player10, player11;
    Button saveBtn;
    int teamNumber;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        // Get views
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

        // Get which team (1 or 2) was selected
        teamNumber = getIntent().getIntExtra("teamNumber", 1);

        // Set page title dynamically
        TextView pageTitle = findViewById(R.id.pageTitle);
        pageTitle.setText("Enter Team " + teamNumber + " Details");

        // Save button click
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tName = teamName.getText().toString().trim();
                String p1 = player1.getText().toString().trim();
                String p2 = player2.getText().toString().trim();
                String p3 = player3.getText().toString().trim();
                String p4 = player4.getText().toString().trim();
                String p5 = player5.getText().toString().trim();
                String p6 = player6.getText().toString().trim();
                String p7 = player7.getText().toString().trim();
                String p8 = player8.getText().toString().trim();
                String p9 = player9.getText().toString().trim();
                String p10 = player10.getText().toString().trim();
                String p11 = player11.getText().toString().trim();


                if (tName.isEmpty() || p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty() || p5.isEmpty()) {
                    Toast.makeText(reset_password.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Here you can save the team details globally or pass them back
                    Toast.makeText(reset_password.this,
                            "Team " + teamNumber + " saved:\n" + tName,
                            Toast.LENGTH_LONG).show();

                    // Close page
                    finish();
                }
            }
        });
    }
}

