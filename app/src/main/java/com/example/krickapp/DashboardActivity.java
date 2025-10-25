package com.example.krickapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface; // Import for Typeface
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout; // Import for ConstraintLayout use

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNav;
    private ImageView profileIcon;

    // Containers for dynamic content
    private LinearLayout highlightsContainer;
    private LinearLayout recentMatchesContainer;
    private LinearLayout scheduledMatchesContainer;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("matches");

        // Initialize views
        profileIcon = findViewById(R.id.profile_icon);
        bottomNav = findViewById(R.id.bottom_nav);

        // Initialize all containers
        highlightsContainer = findViewById(R.id.highlights_container);
        recentMatchesContainer = findViewById(R.id.recent_matches_container);
        scheduledMatchesContainer = findViewById(R.id.scheduled_matches_container);

        // Null safety checks
        if (profileIcon == null || bottomNav == null || highlightsContainer == null
                || recentMatchesContainer == null || scheduledMatchesContainer == null) {
            Toast.makeText(this, "Error loading dashboard or missing container ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Profile icon click - Show logout option
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation logic (already correct)
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_matches) {
                startActivity(new Intent(DashboardActivity.this, MatchesListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(DashboardActivity.this, create_match.class));
                return true;
            } else if (itemId == R.id.navigation_live) {
                Toast.makeText(DashboardActivity.this, "Live Scoring - Coming Soon", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(DashboardActivity.this, MoreActivity.class));
                return true;
            }
            return false;
        });

        // Load data for all sections
        loadHighlights();
        loadRecentMatches();
        loadScheduledMatches();
    }

    // ------------------------------------------------------------------
    // HELPER: Adds a "No Data Found" text message to a given container
    // ------------------------------------------------------------------
    private void addNoDataMessage(LinearLayout container, String message) {
        container.removeAllViews(); // Ensure it's clear before adding the message
        TextView noData = new TextView(this);
        noData.setText(message);
        noData.setPadding(0, (int) (16 * getResources().getDisplayMetrics().density), 0, (int) (16 * getResources().getDisplayMetrics().density));
        noData.setTextColor(0xFF888888); // Gray color
        container.addView(noData);
    }

    // ------------------------------------------------------------------
    // 1. Load Highlights Logic
    // ------------------------------------------------------------------
    private void loadHighlights() {
        highlightsContainer.removeAllViews();
        mDatabase.orderByChild("status").equalTo("highlight").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                highlightsContainer.removeAllViews();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()) {
                        Match match = matchSnapshot.getValue(Match.class);
                        if (match != null) {
                            addHighlightCardToContainer(match);
                        }
                    }
                } else {
                    addNoDataMessage(highlightsContainer, "No match highlights available yet.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                addNoDataMessage(highlightsContainer, "Failed to load highlights.");
            }
        });
    }

    // ------------------------------------------------------------------
    // 2. Load Recent Matches Logic
    // ------------------------------------------------------------------
    private void loadRecentMatches() {
        recentMatchesContainer.removeAllViews();
        // Assuming "completed" matches are recent matches
        mDatabase.orderByChild("status").equalTo("completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentMatchesContainer.removeAllViews();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()) {
                        Match match = matchSnapshot.getValue(Match.class);
                        if (match != null) {
                            addRecentMatchCardToContainer(match);
                        }
                    }
                } else {
                    addNoDataMessage(recentMatchesContainer, "No recent matches found.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                addNoDataMessage(recentMatchesContainer, "Failed to load recent matches.");
            }
        });
    }

    // ------------------------------------------------------------------
    // 3. Load Scheduled Matches Logic (Existing with small refactor)
    // ------------------------------------------------------------------
    private void loadScheduledMatches() {
        scheduledMatchesContainer.removeAllViews();
        mDatabase.orderByChild("status").equalTo("scheduled").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scheduledMatchesContainer.removeAllViews();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()) {
                        Match match = matchSnapshot.getValue(Match.class);
                        if (match != null) {
                            addScheduledMatchCardToContainer(match); // Renamed method for clarity
                        }
                    }
                } else {
                    addNoDataMessage(scheduledMatchesContainer, "No scheduled matches found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                addNoDataMessage(scheduledMatchesContainer, "Failed to load scheduled matches.");
            }
        });
    }

    // ------------------------------------------------------------------
    // CARD CREATION METHODS
    // ------------------------------------------------------------------

    // Method for Highlights - Featured/Important Matches
    private void addHighlightCardToContainer(Match match) {
        // 1. Create a new CardView instance
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        cardView.setLayoutParams(cardParams);

        cardView.setRadius(12 * getResources().getDisplayMetrics().density);
        cardView.setCardElevation(4 * getResources().getDisplayMetrics().density);
        cardView.setCardBackgroundColor(0xFFFFFFFF);

        // 2. Create the internal ConstraintLayout
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        constraintLayout.setPadding(
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density)
        );

        // 3. Create the "LIVE" Badge
        TextView badgeView = new TextView(this);
        badgeView.setId(View.generateViewId());
        badgeView.setText("HIGHLIGHT");
        badgeView.setTextSize(10);
        badgeView.setTextColor(0xFFFFFFFF);
        badgeView.setBackgroundColor(0xFFD32F2F); // Red background
        badgeView.setTypeface(null, Typeface.BOLD);
        badgeView.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (4 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (4 * getResources().getDisplayMetrics().density)
        );

        ConstraintLayout.LayoutParams badgeParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        badgeParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        badgeParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        badgeView.setLayoutParams(badgeParams);

        // 4. Create Venue TextView
        TextView venueView = new TextView(this);
        venueView.setId(View.generateViewId());
        venueView.setText(match.getVenue() != null ? match.getVenue() : "Venue TBD");
        venueView.setTextSize(12);
        venueView.setTextColor(0xFF888888);

        ConstraintLayout.LayoutParams venueParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        venueParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        venueParams.topToBottom = badgeView.getId();
        venueParams.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        venueView.setLayoutParams(venueParams);

        // 5. Create Team 1 Name
        TextView team1View = new TextView(this);
        team1View.setId(View.generateViewId());
        String team1Name = match.getTeam1() != null && match.getTeam1().getTeamName() != null 
                ? match.getTeam1().getTeamName() : "Team 1";
        team1View.setText(team1Name);
        team1View.setTextSize(18);
        team1View.setTextColor(0xFF000000);
        team1View.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams team1Params = new ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        team1Params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team1Params.topToBottom = venueView.getId();
        team1Params.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        team1Params.endToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team1Params.horizontalBias = 0;
        team1View.setLayoutParams(team1Params);

        // 6. Create Team 1 Score (mock data)
        TextView score1View = new TextView(this);
        score1View.setId(View.generateViewId());
        score1View.setText("298-7");
        score1View.setTextSize(16);
        score1View.setTextColor(0xFF000000);
        score1View.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams score1Params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        score1Params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        score1Params.topToTop = team1View.getId();
        score1Params.bottomToBottom = team1View.getId();
        score1View.setLayoutParams(score1Params);

        // 7. Create Team 2 Name
        TextView team2View = new TextView(this);
        team2View.setId(View.generateViewId());
        String team2Name = match.getTeam2() != null && match.getTeam2().getTeamName() != null 
                ? match.getTeam2().getTeamName() : "Team 2";
        team2View.setText(team2Name);
        team2View.setTextSize(18);
        team2View.setTextColor(0xFF000000);
        team2View.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams team2Params = new ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        team2Params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team2Params.topToBottom = team1View.getId();
        team2Params.topMargin = (int) (8 * getResources().getDisplayMetrics().density);
        team2Params.endToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team2Params.horizontalBias = 0;
        team2View.setLayoutParams(team2Params);

        // 8. Create Team 2 Score (mock data)
        TextView score2View = new TextView(this);
        score2View.setText("156-3");
        score2View.setTextSize(16);
        score2View.setTextColor(0xFF000000);
        score2View.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams score2Params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        score2Params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        score2Params.topToTop = team2View.getId();
        score2Params.bottomToBottom = team2View.getId();
        score2View.setLayoutParams(score2Params);

        // 9. Add all views to ConstraintLayout
        constraintLayout.addView(badgeView);
        constraintLayout.addView(venueView);
        constraintLayout.addView(team1View);
        constraintLayout.addView(score1View);
        constraintLayout.addView(team2View);
        constraintLayout.addView(score2View);

        // 10. Add ConstraintLayout to CardView
        cardView.addView(constraintLayout);

        // 11. Set click listener to open match details
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, matchinfo.class);
            intent.putExtra("matchId", match.getMatchId());
            startActivity(intent);
        });

        // 12. Add CardView to the container
        highlightsContainer.addView(cardView);
    }

    // Method for Recent Matches - Completed Matches
    private void addRecentMatchCardToContainer(Match match) {
        // 1. Create a new CardView instance
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, (int) (10 * getResources().getDisplayMetrics().density));
        cardView.setLayoutParams(cardParams);

        cardView.setRadius(8 * getResources().getDisplayMetrics().density);
        cardView.setCardElevation(2 * getResources().getDisplayMetrics().density);
        cardView.setCardBackgroundColor(0xFFFFFFFF);

        // 2. Create the internal ConstraintLayout
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        constraintLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density)
        );

        // 3. Create the "COMPLETED" Badge
        TextView badgeView = new TextView(this);
        badgeView.setId(View.generateViewId());
        badgeView.setText("COMPLETED");
        badgeView.setTextSize(10);
        badgeView.setTextColor(0xFFFFFFFF);
        badgeView.setBackgroundColor(0xFF4CAF50); // Green background
        badgeView.setTypeface(null, Typeface.BOLD);
        badgeView.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (4 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (4 * getResources().getDisplayMetrics().density)
        );

        ConstraintLayout.LayoutParams badgeParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        badgeParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        badgeParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        badgeView.setLayoutParams(badgeParams);

        // 4. Create Date TextView
        TextView dateView = new TextView(this);
        dateView.setId(View.generateViewId());
        dateView.setText(match.getDate() != null ? match.getDate() : "Date TBD");
        dateView.setTextSize(12);
        dateView.setTextColor(0xFF888888);

        ConstraintLayout.LayoutParams dateParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        dateParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        dateParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        dateView.setLayoutParams(dateParams);

        // 5. Create Venue TextView
        TextView venueView = new TextView(this);
        venueView.setId(View.generateViewId());
        venueView.setText(match.getVenue() != null ? match.getVenue() : "Venue TBD");
        venueView.setTextSize(12);
        venueView.setTextColor(0xFF888888);

        ConstraintLayout.LayoutParams venueParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        venueParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        venueParams.topToBottom = badgeView.getId();
        venueParams.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        venueView.setLayoutParams(venueParams);

        // 6. Create Team 1 Name and Score
        TextView team1View = new TextView(this);
        team1View.setId(View.generateViewId());
        String team1Name = match.getTeam1() != null && match.getTeam1().getTeamName() != null 
                ? match.getTeam1().getTeamName() : "Team 1";
        team1View.setText(team1Name);
        team1View.setTextSize(16);
        team1View.setTextColor(0xFF000000);
        team1View.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams team1Params = new ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        team1Params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team1Params.topToBottom = venueView.getId();
        team1Params.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        team1Params.endToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team1Params.horizontalBias = 0;
        team1View.setLayoutParams(team1Params);

        TextView score1View = new TextView(this);
        score1View.setId(View.generateViewId());
        score1View.setText("287-8");
        score1View.setTextSize(14);
        score1View.setTextColor(0xFF000000);

        ConstraintLayout.LayoutParams score1Params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        score1Params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        score1Params.topToTop = team1View.getId();
        score1Params.bottomToBottom = team1View.getId();
        score1View.setLayoutParams(score1Params);

        // 7. Create Team 2 Name and Score
        TextView team2View = new TextView(this);
        team2View.setId(View.generateViewId());
        String team2Name = match.getTeam2() != null && match.getTeam2().getTeamName() != null 
                ? match.getTeam2().getTeamName() : "Team 2";
        team2View.setText(team2Name);
        team2View.setTextSize(16);
        team2View.setTextColor(0xFF000000);
        team2View.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams team2Params = new ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        team2Params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team2Params.topToBottom = team1View.getId();
        team2Params.topMargin = (int) (8 * getResources().getDisplayMetrics().density);
        team2Params.endToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        team2Params.horizontalBias = 0;
        team2View.setLayoutParams(team2Params);

        TextView score2View = new TextView(this);
        score2View.setText("245-10");
        score2View.setTextSize(14);
        score2View.setTextColor(0xFF000000);

        ConstraintLayout.LayoutParams score2Params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        score2Params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        score2Params.topToTop = team2View.getId();
        score2Params.bottomToBottom = team2View.getId();
        score2View.setLayoutParams(score2Params);

        // 8. Create Result TextView
        TextView resultView = new TextView(this);
        resultView.setText(team1Name + " won by 42 runs");
        resultView.setTextSize(12);
        resultView.setTextColor(0xFF1976D2); // Blue color
        resultView.setTypeface(null, Typeface.BOLD);

        ConstraintLayout.LayoutParams resultParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        resultParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        resultParams.topToBottom = team2View.getId();
        resultParams.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        resultView.setLayoutParams(resultParams);

        // 9. Add all views to ConstraintLayout
        constraintLayout.addView(badgeView);
        constraintLayout.addView(dateView);
        constraintLayout.addView(venueView);
        constraintLayout.addView(team1View);
        constraintLayout.addView(score1View);
        constraintLayout.addView(team2View);
        constraintLayout.addView(score2View);
        constraintLayout.addView(resultView);

        // 10. Add ConstraintLayout to CardView
        cardView.addView(constraintLayout);

        // 11. Set click listener to open match details
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, matchinfo.class);
            intent.putExtra("matchId", match.getMatchId());
            startActivity(intent);
        });

        // 12. Add CardView to the container
        recentMatchesContainer.addView(cardView);
    }

    // Renamed and updated existing method for Scheduled Matches
    private void addScheduledMatchCardToContainer(Match match) {
        // Null safety check for match and teams
        if (match == null || match.getTeam1() == null || match.getTeam2() == null) {
            return;
        }
        
        // 1. Create a new CardView instance
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, (int) (10 * getResources().getDisplayMetrics().density)); // Converted 10dp margin
        cardView.setLayoutParams(cardParams);

        cardView.setRadius(8 * getResources().getDisplayMetrics().density);
        cardView.setCardElevation(2 * getResources().getDisplayMetrics().density);
        cardView.setCardBackgroundColor(0xFFFFFFFF);

        // 2. Create the internal ConstraintLayout
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        constraintLayout.setPadding((int) (16 * getResources().getDisplayMetrics().density), (int) (16 * getResources().getDisplayMetrics().density), (int) (16 * getResources().getDisplayMetrics().density), (int) (16 * getResources().getDisplayMetrics().density)); // Converted 16dp padding

        // 3. Create the Title TextView with null checks
        TextView titleView = new TextView(this);
        titleView.setId(View.generateViewId());
        String team1Name = match.getTeam1().getTeamName() != null ? match.getTeam1().getTeamName() : "Team 1";
        String team2Name = match.getTeam2().getTeamName() != null ? match.getTeam2().getTeamName() : "Team 2";
        titleView.setText(team1Name + " vs. " + team2Name);
        titleView.setTextSize(16);
        titleView.setTextColor(0xFF000000);

        ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        titleParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        titleView.setLayoutParams(titleParams);

        // 4. Create the Time TextView with null checks
        TextView timeView = new TextView(this);
        String date = match.getDate() != null ? match.getDate() : "TBD";
        String time = match.getTime() != null ? match.getTime() : "TBD";
        timeView.setText(date + ", " + time);
        timeView.setTextSize(14);
        timeView.setTextColor(0xFF888888);

        ConstraintLayout.LayoutParams timeParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        timeParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        timeParams.topToBottom = titleView.getId();
        timeView.setLayoutParams(timeParams);

        // 5. Create the Status TextView
        TextView statusView = new TextView(this);
        statusView.setText("Upcoming");
        statusView.setTextColor(0xFF304FFE);
        statusView.setTypeface(null, Typeface.BOLD); // CORRECT WAY to set bold text style
        statusView.setPadding(4, 4, 4, 4);

        ConstraintLayout.LayoutParams statusParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        statusParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        statusParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        statusParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        statusView.setLayoutParams(statusParams);

        // 6. Add views to the ConstraintLayout
        constraintLayout.addView(titleView);
        constraintLayout.addView(timeView);
        constraintLayout.addView(statusView);

        // 7. Add ConstraintLayout to CardView
        cardView.addView(constraintLayout);

        // 8. Set click listener to open match details
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, matchinfo.class);
            intent.putExtra("matchId", match.getMatchId());
            startActivity(intent);
        });

        // 9. Add CardView to the main container
        scheduledMatchesContainer.addView(cardView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    // ... (rest of the showLogoutDialog method)
    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    mAuth.signOut();
                    Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
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