package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesListActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private MatchesAdapter adapter;
    private List<Match> matchesList;
    private List<Match> allMatches;
    private DatabaseReference mDatabase;
    private LinearLayout emptyState;
    private ValueEventListener matchesListener;
    
    private TextView tabUpcoming, tabLive, tabCompleted;
    private Button btnFlipCoin;
    private String currentTab = "live";
    
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_matches);
        emptyState = findViewById(R.id.empty_state);
        btnFlipCoin = findViewById(R.id.btn_flip_coin);
        
        tabUpcoming = findViewById(R.id.tab_upcoming);
        tabLive = findViewById(R.id.tab_live);
        tabCompleted = findViewById(R.id.tab_completed);
        
        bottomNav = findViewById(R.id.bottom_nav);
        fabCreate = findViewById(R.id.fab_create);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchesList = new ArrayList<>();
        allMatches = new ArrayList<>();
        adapter = new MatchesAdapter(matchesList, this);
        recyclerView.setAdapter(adapter);
        
        // Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference("matches");
        
        // Tab click listeners
        tabUpcoming.setOnClickListener(v -> selectTab("upcoming"));
        tabLive.setOnClickListener(v -> selectTab("live"));
        tabCompleted.setOnClickListener(v -> selectTab("completed"));
        
        // Flip coin button
        btnFlipCoin.setOnClickListener(v -> {
            startActivity(new Intent(this, tossresult.class));
        });
        
        // Setup bottom navigation
        bottomNav.setSelectedItemId(R.id.navigation_matches);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
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
        
        fabCreate.setOnClickListener(v -> {
            startActivity(new Intent(this, create_match.class));
        });
        
        // Load matches
        loadMatches();
    }
    
    private void selectTab(String tab) {
        currentTab = tab;
        
        // Reset all tabs
        tabUpcoming.setBackgroundResource(R.drawable.tab_unselected_background);
        tabUpcoming.setTextColor(getColor(R.color.text_secondary));
        tabUpcoming.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        tabLive.setBackgroundResource(R.drawable.tab_unselected_background);
        tabLive.setTextColor(getColor(R.color.text_secondary));
        tabLive.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        tabCompleted.setBackgroundResource(R.drawable.tab_unselected_background);
        tabCompleted.setTextColor(getColor(R.color.text_secondary));
        tabCompleted.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        // Set selected tab
        switch (tab) {
            case "upcoming":
                tabUpcoming.setBackgroundResource(R.drawable.tab_selected_background);
                tabUpcoming.setTextColor(getColor(R.color.text_primary));
                tabUpcoming.setTypeface(null, android.graphics.Typeface.BOLD);
                btnFlipCoin.setVisibility(View.GONE);
                break;
            case "live":
                tabLive.setBackgroundResource(R.drawable.tab_selected_background);
                tabLive.setTextColor(getColor(R.color.text_primary));
                tabLive.setTypeface(null, android.graphics.Typeface.BOLD);
                btnFlipCoin.setVisibility(View.VISIBLE);
                break;
            case "completed":
                tabCompleted.setBackgroundResource(R.drawable.tab_selected_background);
                tabCompleted.setTextColor(getColor(R.color.text_primary));
                tabCompleted.setTypeface(null, android.graphics.Typeface.BOLD);
                btnFlipCoin.setVisibility(View.GONE);
                break;
        }
        
        filterMatches();
    }
    
    private void loadMatches() {
        matchesListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allMatches.clear();
                
                for (DataSnapshot matchSnapshot : snapshot.getChildren()) {
                    try {
                        Match match = matchSnapshot.getValue(Match.class);
                        if (match != null) {
                            match.setMatchId(matchSnapshot.getKey());
                            allMatches.add(match);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                filterMatches();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MatchesListActivity.this, 
                    "Failed to load matches: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void filterMatches() {
        matchesList.clear();
        
        for (Match match : allMatches) {
            String status = match.getStatus() != null ? match.getStatus().toLowerCase() : "scheduled";
            
            if (currentTab.equals("upcoming") && status.equals("scheduled")) {
                matchesList.add(match);
            } else if (currentTab.equals("live") && (status.equals("ongoing") || status.equals("live"))) {
                matchesList.add(match);
            } else if (currentTab.equals("completed") && status.equals("completed")) {
                matchesList.add(match);
            }
        }
        
        adapter.notifyDataSetChanged();
        
        // Show/hide empty state
        if (matchesList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (matchesListener != null && mDatabase != null) {
            mDatabase.removeEventListener(matchesListener);
        }
    }
}
