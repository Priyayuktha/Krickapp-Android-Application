package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class matchinfo extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FloatingActionButton fabCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchinfo);

        // Back button
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);
        fabCreate = findViewById(R.id.fab_create);
        
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
                Toast.makeText(this, "More options coming soon", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        });
        
        fabCreate.setOnClickListener(v -> {
            startActivity(new Intent(this, create_match.class));
        });

        // Setup rows with icons + text
        setupRow(R.id.rowMatchType, R.drawable.matchtype, "Match Type", "T20");
        setupRow(R.id.rowDate, R.drawable.date, "Date", "28 August 2025");
        setupRow(R.id.rowTime, R.drawable.time, "Time", "6:00 PM (IST)");
        setupRow(R.id.rowVenue, R.drawable.venue, "Venue", "Green Park Stadium");
        setupRow(R.id.rowCity, R.drawable.city, "City", "Kanpur");
        setupRow(R.id.rowToss, R.drawable.toos, "Toss Win", "Team A choose Batting");
        setupRow(R.id.rowHost, R.drawable.host, "Host", "Local Cricket Association");
    }

    private void setupRow(int rowId, int iconRes, String label, String value) {
        LinearLayout row = findViewById(rowId);
        ImageView icon = row.findViewById(R.id.rowIcon);
        TextView lbl = row.findViewById(R.id.rowLabel);
        TextView val = row.findViewById(R.id.rowValue);

        icon.setImageResource(iconRes);
        lbl.setText(label);
        val.setText(value);
    }
}