package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MoreActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private LinearLayout menuAdminProfile, menuAbout, menuTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        // Initialize views
        menuAdminProfile = findViewById(R.id.menu_admin_profile);
        menuAbout = findViewById(R.id.menu_about);
        menuTerms = findViewById(R.id.menu_terms);

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setSelectedItemId(R.id.navigation_more);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                Intent intent = new Intent(this, MatchesListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(this, create_match.class));
                return true;
            } else if (itemId == R.id.navigation_live) {
                Intent intent = new Intent(this, MatchesListActivity.class);
                intent.putExtra("filterStatus", "live");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_more) {
                // Already on more screen
                return true;
            }
            return false;
        });

        // Menu click listeners
        menuAdminProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
        });

        menuAbout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });

        menuTerms.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsActivity.class));
        });
    }
}
