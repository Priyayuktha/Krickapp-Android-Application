package com.example.krickapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class matchresult extends AppCompatActivity {

    private Button btnTeam1, btnTeam2, btnCancel, btnOk;
    private CheckBox cbNRR, cbDraw, cbAbandoned;
    private EditText etResultInfo;

    private String winner = "";   // store winner
    private String matchId;
    private String team1Name = "Team 1";
    private String team2Name = "Team 2";
    
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchresult);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get match data from intent
        Intent intent = getIntent();
        if (intent != null) {
            matchId = intent.getStringExtra("matchId");
            team1Name = intent.getStringExtra("team1Name") != null ? 
                       intent.getStringExtra("team1Name") : team1Name;
            team2Name = intent.getStringExtra("team2Name") != null ? 
                       intent.getStringExtra("team2Name") : team2Name;
        }

        // Initialize
        btnTeam1 = findViewById(R.id.btnTeam1);
        btnTeam2 = findViewById(R.id.btnTeam2);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        cbNRR = findViewById(R.id.cbNRR);
        cbDraw = findViewById(R.id.cbDraw);
        cbAbandoned = findViewById(R.id.cbAbandoned);
        etResultInfo = findViewById(R.id.etResultInfo);

        // Set team names on buttons
        btnTeam1.setText(team1Name);
        btnTeam2.setText(team2Name);

        // --- TEAM 1 ---
        btnTeam1.setOnClickListener(v -> {
            winner = team1Name;
            etResultInfo.setText(winner + " won the match");
            clearCheckBoxes(); // clear draw/abandoned if selecting winner
            Toast.makeText(this, team1Name + " Selected", Toast.LENGTH_SHORT).show();
        });

        // --- TEAM 2 ---
        btnTeam2.setOnClickListener(v -> {
            winner = team2Name;
            etResultInfo.setText(winner + " won the match");
            clearCheckBoxes();
            Toast.makeText(this, team2Name + " Selected", Toast.LENGTH_SHORT).show();
        });

        // --- CHECKBOXES ---
        cbDraw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbAbandoned.setChecked(false);
                cbNRR.setChecked(false);
                winner = "";
                etResultInfo.setText("Match Drawn");
            }
        });

        cbAbandoned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbDraw.setChecked(false);
                cbNRR.setChecked(false);
                winner = "";
                etResultInfo.setText("Match Abandoned");
            }
        });

        cbNRR.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbDraw.setChecked(false);
                cbAbandoned.setChecked(false);
                // Keep current winner text, just add NRR note
                if (!winner.isEmpty()) {
                    etResultInfo.setText(winner + " won the match (consider NRR)");
                } else {
                    etResultInfo.setText("Consider all overs for NRR calculation");
                }
            }
        });

        // --- CANCEL BUTTON ---
        btnCancel.setOnClickListener(v -> finish());

        // --- OK BUTTON ---
        btnOk.setOnClickListener(v -> {
            String result = etResultInfo.getText().toString();
            if (result.isEmpty()) {
                Toast.makeText(this, "Please select a result!", Toast.LENGTH_SHORT).show();
            } else {
                // Save to Firebase
                saveMatchResultToFirebase(result);
            }
        });

        // --- Bottom Nav ---
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                Intent homeIntent = new Intent(matchresult.this, DashboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (id == R.id.navigation_matches) {
                Intent matchesIntent = new Intent(matchresult.this, MatchesListActivity.class);
                matchesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(matchesIntent);
                finish();
                return true;
            } else if (id == R.id.navigation_create) {
                startActivity(new Intent(matchresult.this, create_match.class));
                return true;
            } else if (id == R.id.navigation_live) {
                Intent liveIntent = new Intent(matchresult.this, MatchesListActivity.class);
                liveIntent.putExtra("filterStatus", "live");
                liveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(liveIntent);
                finish();
                return true;
            } else if (id == R.id.navigation_more) {
                startActivity(new Intent(matchresult.this, MoreActivity.class));
                return true;
            }
            return false;
        });
    }

    // helper to clear checkboxes when selecting a winner
    private void clearCheckBoxes() {
        cbDraw.setChecked(false);
        cbAbandoned.setChecked(false);
        // do not clear NRR, allow user to tick it with winner
    }

    // Save match result to Firebase
    private void saveMatchResultToFirebase(String result) {
        if (matchId == null || matchId.isEmpty()) {
            Toast.makeText(this, "No match ID available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create summary data
        java.util.HashMap<String, Object> summaryMap = new java.util.HashMap<>();
        summaryMap.put("matchResult", result);
        summaryMap.put("winner", winner);
        summaryMap.put("isDraw", cbDraw.isChecked());
        summaryMap.put("isAbandoned", cbAbandoned.isChecked());
        summaryMap.put("considerNRR", cbNRR.isChecked());

        // Save to Firebase
        mDatabase.child("matches").child(matchId).child("summary").updateChildren(summaryMap)
            .addOnSuccessListener(aVoid -> {
                // Update match status to completed
                mDatabase.child("matches").child(matchId).child("status").setValue("completed")
                    .addOnSuccessListener(aVoid2 -> {
                        Toast.makeText(this, "Match Result Saved!", Toast.LENGTH_SHORT).show();
                        
                        // Navigate to Match Summary page
                        MatchSummaryHelper.launchMatchSummary(
                            matchresult.this,
                            matchId,
                            team1Name,
                            team2Name,
                            result,
                            "To be decided" // Player of match can be updated later
                        );
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update status: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    });
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to save result: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
    }
}
