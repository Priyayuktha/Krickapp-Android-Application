package com.example.krickapp;

import android.annotation.SuppressLint;
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

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        profileIcon = findViewById(R.id.profile_icon);
        bottomNav = findViewById(R.id.bottom_nav);

        // Null safety checks
        if (profileIcon == null || bottomNav == null) {
            Toast.makeText(this, "Error loading dashboard", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Profile icon click - Show logout option
        profileIcon.setOnClickListener(v -> {
            showLogoutDialog();
        });

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.navigation_home) {
                // Already on home
                return true;
            } else if (itemId == R.id.navigation_matches) {
                startActivity(new Intent(DashboardActivity.this, MatchesListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_create) {
                // Navigate to create match
                startActivity(new Intent(DashboardActivity.this, create_match.class));
                return true;
            } else if (itemId == R.id.navigation_live) {
                Toast.makeText(DashboardActivity.this, "Live Scoring - Coming Soon", Toast.LENGTH_SHORT).show();
                // Navigate to live scoring activity when created
                return true;
            } else if (itemId == R.id.navigation_more) {
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
        super.onBackPressed();
        moveTaskToBack(true);
    }
    
    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout", (dialog, which) -> {
                // Sign out from Firebase
                mAuth.signOut();
                
                Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                
                // Navigate back to MainActivity
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            })
            .show();
    }
}
