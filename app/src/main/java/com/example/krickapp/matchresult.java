package com.example.krickapp;


import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class matchresult extends AppCompatActivity {

    private Button btnTeam1, btnTeam2, btnCancel, btnOk;
    private CheckBox cbNRR, cbDraw, cbAbandoned;
    private EditText etResultInfo;

    private String winner = "";   // store winner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchresult);

        // Initialize
        btnTeam1 = findViewById(R.id.btnTeam1);
        btnTeam2 = findViewById(R.id.btnTeam2);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        cbNRR = findViewById(R.id.cbNRR);
        cbDraw = findViewById(R.id.cbDraw);
        cbAbandoned = findViewById(R.id.cbAbandoned);
        etResultInfo = findViewById(R.id.etResultInfo);

        // --- TEAM 1 ---
        btnTeam1.setOnClickListener(v -> {
            winner = "Team 1";
            etResultInfo.setText(winner + " won the match");
            clearCheckBoxes(); // clear draw/abandoned if selecting winner
            Toast.makeText(this, "Team 1 Selected", Toast.LENGTH_SHORT).show();
        });

        // --- TEAM 2 ---
        btnTeam2.setOnClickListener(v -> {
            winner = "Team 2";
            etResultInfo.setText(winner + " won the match");
            clearCheckBoxes();
            Toast.makeText(this, "Team 2 Selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Match Result Saved: " + result, Toast.LENGTH_LONG).show();
                // 👉 Later: save to DB or pass via intent
            }
        });

        // --- Bottom Nav ---
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_matches) {
                Toast.makeText(this, "Matches", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_live) {
                Toast.makeText(this, "Live", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_more) {
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // --- FAB ---
        FloatingActionButton fab = findViewById(R.id.fab_create);
        fab.setOnClickListener(v ->
                Toast.makeText(this, "Create New Match", Toast.LENGTH_SHORT).show());
    }

    // helper to clear checkboxes when selecting a winner
    private void clearCheckBoxes() {
        cbDraw.setChecked(false);
        cbAbandoned.setChecked(false);
        // do not clear NRR, allow user to tick it with winner
    }
}
