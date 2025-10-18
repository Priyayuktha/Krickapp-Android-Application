package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeamSelectionActivity extends AppCompatActivity {

    private String matchName, venue, date, time, matchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get match data from previous activity
        Intent intent = getIntent();
        matchName = intent.getStringExtra("matchName");
        venue = intent.getStringExtra("venue");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        matchType = intent.getStringExtra("matchType");

        // Directly open team details page with both teams
        Intent teamIntent = new Intent(TeamSelectionActivity.this, reset_password.class);
        teamIntent.putExtra("matchName", matchName);
        teamIntent.putExtra("venue", venue);
        teamIntent.putExtra("date", date);
        teamIntent.putExtra("time", time);
        teamIntent.putExtra("matchType", matchType);
        startActivity(teamIntent);
        
        // Close this activity as we're directly going to team details
        finish();
    }
}
