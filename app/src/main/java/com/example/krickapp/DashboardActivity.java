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
                Toast.makeText(DashboardActivity.this, "More", Toast.LENGTH_SHORT).show();
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

    // Placeholder method for Highlights - Implement this to match your card structure
    private void addHighlightCardToContainer(Match match) {
        // You can copy the code from addScheduledMatchCardToContainer and adapt it
        // to match the appearance and function of your Highlight Card.
    }

    // Placeholder method for Recent Matches - Implement this to match your card structure
    private void addRecentMatchCardToContainer(Match match) {
        // You can copy the code from addScheduledMatchCardToContainer and adapt it
        // to match the appearance and function of your Recent Match Card.
    }

    // Renamed and updated existing method for Scheduled Matches
    private void addScheduledMatchCardToContainer(Match match) {
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

        // 3. Create the Title TextView
        TextView titleView = new TextView(this);
        titleView.setId(View.generateViewId());
        titleView.setText(match.getTeam1().getTeamName() + " vs. " + match.getTeam2().getTeamName());
        titleView.setTextSize(16);
        titleView.setTextColor(0xFF000000);

        ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        titleParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        titleView.setLayoutParams(titleParams);

        // 4. Create the Time TextView
        TextView timeView = new TextView(this);
        timeView.setText(match.getDate() + ", " + match.getTime());
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

        // 8. Add CardView to the main container
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