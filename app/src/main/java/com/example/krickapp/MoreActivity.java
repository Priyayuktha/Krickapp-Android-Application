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
    private FloatingActionButton fabCreate;
    private LinearLayout menuAdminProfile, menuAbout, menuTerms, menuSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        // Initialize views
        menuAdminProfile = findViewById(R.id.menu_admin_profile);
        menuAbout = findViewById(R.id.menu_about);
        menuTerms = findViewById(R.id.menu_terms);
        menuSupport = findViewById(R.id.menu_support);

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);
        fabCreate = findViewById(R.id.fab_create);
        
        bottomNav.setSelectedItemId(R.id.navigation_more);
        
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
                return true;
            }
            return false;
        });
        
        fabCreate.setOnClickListener(v -> {
            startActivity(new Intent(this, create_match.class));
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

        menuSupport.setOnClickListener(v -> {
            Toast.makeText(this, "Support: support@krickapp.com", Toast.LENGTH_LONG).show();
        });
    }
}
