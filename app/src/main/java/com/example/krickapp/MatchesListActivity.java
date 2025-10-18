package com.example.krickapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private DatabaseReference mDatabase;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout emptyState;
    
    private Button btnAll, btnScheduled, btnOngoing, btnCompleted;
    private String currentFilter = "all";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_matches);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        emptyState = findViewById(R.id.empty_state);
        ImageView btnBack = findViewById(R.id.btn_back);
        
        btnAll = findViewById(R.id.btn_all);
        btnScheduled = findViewById(R.id.btn_scheduled);
        btnOngoing = findViewById(R.id.btn_ongoing);
        btnCompleted = findViewById(R.id.btn_completed);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchesList = new ArrayList<>();
        adapter = new MatchesAdapter(matchesList);
        recyclerView.setAdapter(adapter);
        
        // Setup click listener for match items
        adapter.setOnMatchClickListener(match -> {
            Toast.makeText(MatchesListActivity.this, 
                "Match: " + match.getMatchName(), Toast.LENGTH_SHORT).show();
            // TODO: Navigate to match details activity
        });
        
        // Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference("matches");
        
        // Back button
        btnBack.setOnClickListener(v -> finish());
        
        // Swipe to refresh
        swipeRefresh.setOnRefreshListener(this::loadMatches);
        
        // Filter buttons
        btnAll.setOnClickListener(v -> applyFilter("all"));
        btnScheduled.setOnClickListener(v -> applyFilter("scheduled"));
        btnOngoing.setOnClickListener(v -> applyFilter("ongoing"));
        btnCompleted.setOnClickListener(v -> applyFilter("completed"));
        
        // Load matches
        loadMatches();
    }
    
    private void loadMatches() {
        swipeRefresh.setRefreshing(true);
        
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchesList.clear();
                
                for (DataSnapshot matchSnapshot : snapshot.getChildren()) {
                    try {
                        Match match = matchSnapshot.getValue(Match.class);
                        if (match != null) {
                            match.setMatchId(matchSnapshot.getKey());
                            matchesList.add(match);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                adapter.updateData(matchesList);
                applyFilter(currentFilter);
                swipeRefresh.setRefreshing(false);
                
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
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(MatchesListActivity.this, 
                    "Failed to load matches: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void applyFilter(String filter) {
        currentFilter = filter;
        adapter.filterByStatus(filter);
        
        // Update button colors
        resetFilterButtons();
        
        switch (filter) {
            case "all":
                btnAll.setBackgroundColor(Color.parseColor("#FFD600"));
                break;
            case "scheduled":
                btnScheduled.setBackgroundColor(Color.parseColor("#FFD600"));
                break;
            case "ongoing":
                btnOngoing.setBackgroundColor(Color.parseColor("#FFD600"));
                break;
            case "completed":
                btnCompleted.setBackgroundColor(Color.parseColor("#FFD600"));
                break;
        }
    }
    
    private void resetFilterButtons() {
        int grayColor = Color.parseColor("#E0E0E0");
        btnAll.setBackgroundColor(grayColor);
        btnScheduled.setBackgroundColor(grayColor);
        btnOngoing.setBackgroundColor(grayColor);
        btnCompleted.setBackgroundColor(grayColor);
    }
}
