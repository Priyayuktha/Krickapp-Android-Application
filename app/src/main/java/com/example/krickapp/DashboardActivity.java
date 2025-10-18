package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNav;
    private ImageView profileIcon;
    private CardView createMatchCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        profileIcon = findViewById(R.id.profile_icon);
        bottomNav = findViewById(R.id.bottomNav);

        // Profile icon click
        profileIcon.setOnClickListener(v -> {
            Toast.makeText(DashboardActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
            // Navigate to profile activity when created
        });

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                // Already on home
                return true;
            } else if (itemId == R.id.nav_matches) {
                startActivity(new Intent(DashboardActivity.this, MatchesListActivity.class));
                return true;
            } else if (itemId == R.id.nav_live) {
                // Navigate to create match or live scoring
                startActivity(new Intent(DashboardActivity.this, create_match.class));
                return true;
            } else if (itemId == R.id.nav_more) {
                Toast.makeText(DashboardActivity.this, "More", Toast.LENGTH_SHORT).show();
                // Navigate to more/settings activity when created
                return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to login screen
        moveTaskToBack(true);
    }
}
