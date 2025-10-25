package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);

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
                startActivity(new Intent(this, MoreActivity.class));
                finish();
                return true;
            }
            return false;
        });

    }
}
