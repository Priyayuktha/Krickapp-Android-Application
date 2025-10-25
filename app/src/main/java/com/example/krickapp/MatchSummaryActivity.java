package com.example.krickapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MatchSummaryActivity extends AppCompatActivity {

    private TextView tvMatchResult;
    private TextView tvTeamsLabel;
    private TextView tvPlayerOfMatch;
    private Button btnTeam1;
    private Button btnTeam2;
    private LinearLayout overDetailsContainer;
    private BottomNavigationView bottomNav;

    // Sample data - replace with actual data from Firebase or intent extras
    private String team1Name = "Team 1";
    private String team2Name = "Team 2";
    private String matchResult = "Team 1 won by 14 runs";
    private String playerOfMatch = "Tim David";
    private int selectedTeam = 1; // 1 for Team 1, 2 for Team 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_summary);

        // Initialize views
        initializeViews();

        // Get data from intent if available
        getIntentData();

        // Set up UI
        setupUI();

        // Set up bottom navigation
        setupBottomNavigation();

        // Set up team button listeners
        setupTeamButtons();
    }

    private void initializeViews() {
        tvMatchResult = findViewById(R.id.tvMatchResult);
        tvTeamsLabel = findViewById(R.id.tvTeamsLabel);
        tvPlayerOfMatch = findViewById(R.id.tvPlayerOfMatch);
        btnTeam1 = findViewById(R.id.btnTeam1);
        btnTeam2 = findViewById(R.id.btnTeam2);
        overDetailsContainer = findViewById(R.id.overDetailsContainer);
        bottomNav = findViewById(R.id.bottom_nav);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            team1Name = intent.getStringExtra("team1Name") != null ? 
                        intent.getStringExtra("team1Name") : team1Name;
            team2Name = intent.getStringExtra("team2Name") != null ? 
                        intent.getStringExtra("team2Name") : team2Name;
            matchResult = intent.getStringExtra("matchResult") != null ? 
                          intent.getStringExtra("matchResult") : matchResult;
            playerOfMatch = intent.getStringExtra("playerOfMatch") != null ? 
                            intent.getStringExtra("playerOfMatch") : playerOfMatch;
            
            // Check if full data is available
            boolean hasFullData = intent.getBooleanExtra("hasFullData", false);
            if (hasFullData) {
                MatchSummaryData data = MatchSummaryHelper.MatchSummaryDataHolder.getInstance();
                if (data != null) {
                    loadMatchSummaryData(data);
                    MatchSummaryHelper.MatchSummaryDataHolder.clearInstance();
                }
            }
        }
    }

    private void setupUI() {
        // Set match result
        tvMatchResult.setText(matchResult);

        // Set teams label
        tvTeamsLabel.setText(team1Name + " vs " + team2Name);

        // Set player of the match
        tvPlayerOfMatch.setText(playerOfMatch);

        // Set team button names
        btnTeam1.setText(team1Name);
        btnTeam2.setText(team2Name);

        // Update team button selection
        updateTeamButtonSelection();
    }

    private void setupTeamButtons() {
        btnTeam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTeam = 1;
                updateTeamButtonSelection();
                // Load Team 1 over details here
            }
        });

        btnTeam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTeam = 2;
                updateTeamButtonSelection();
                // Load Team 2 over details here
            }
        });
    }

    private void updateTeamButtonSelection() {
        if (selectedTeam == 1) {
            // Team 1 selected (yellow)
            btnTeam1.setBackgroundResource(R.drawable.btn_team_yellow);
            btnTeam1.setTextColor(Color.BLACK);
            
            // Team 2 unselected (grey)
            btnTeam2.setBackgroundResource(R.drawable.circle_grey);
            btnTeam2.setTextColor(Color.BLACK);
        } else {
            // Team 2 selected (yellow)
            btnTeam2.setBackgroundResource(R.drawable.btn_team_yellow);
            btnTeam2.setTextColor(Color.BLACK);
            
            // Team 1 unselected (grey)
            btnTeam1.setBackgroundResource(R.drawable.circle_grey);
            btnTeam1.setTextColor(Color.BLACK);
        }
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.navigation_matches);
        
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(MatchSummaryActivity.this, DashboardActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_matches) {
                    startActivity(new Intent(MatchSummaryActivity.this, MatchesListActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_create) {
                    startActivity(new Intent(MatchSummaryActivity.this, create_match.class));
                    return true;
                } else if (itemId == R.id.navigation_live) {
                    // Navigate to Live activity (create if needed)
                    return true;
                } else if (itemId == R.id.navigation_more) {
                    startActivity(new Intent(MatchSummaryActivity.this, MoreActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    // Method to dynamically add over details (call this with actual data)
    public void addOverDetail(String overText, int[] ballRuns, boolean[] isWicket, boolean[] isSix, boolean[] isFour) {
        LinearLayout overLayout = new LinearLayout(this);
        overLayout.setOrientation(LinearLayout.HORIZONTAL);
        overLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams overLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        overLayout.setLayoutParams(overLayoutParams);
        overLayout.setPadding(0, dpToPx(12), 0, dpToPx(12));

        // Over text
        TextView overTextView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        overTextView.setLayoutParams(textParams);
        overTextView.setText(overText);
        overTextView.setTextSize(14);
        overTextView.setTextColor(Color.BLACK);
        overTextView.setMaxLines(1);
        overLayout.addView(overTextView);

        // Ball sequence container
        LinearLayout ballContainer = new LinearLayout(this);
        ballContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams ballContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ballContainerParams.setMarginStart(dpToPx(8));
        ballContainer.setLayoutParams(ballContainerParams);

        // Add balls
        for (int i = 0; i < ballRuns.length && i < 6; i++) {
            TextView ballView = new TextView(this);
            LinearLayout.LayoutParams ballParams = new LinearLayout.LayoutParams(
                    dpToPx(36),
                    dpToPx(36)
            );
            ballParams.setMarginEnd(dpToPx(4));
            ballView.setLayoutParams(ballParams);
            ballView.setGravity(android.view.Gravity.CENTER);
            ballView.setTextSize(14);
            ballView.setTypeface(null, android.graphics.Typeface.BOLD);

            if (isWicket[i]) {
                ballView.setText("W");
                ballView.setBackgroundResource(R.drawable.circle_red);
                ballView.setTextColor(Color.WHITE);
            } else if (isSix[i]) {
                ballView.setText(String.valueOf(ballRuns[i]));
                ballView.setBackgroundResource(R.drawable.circle_blue);
                ballView.setTextColor(Color.WHITE);
            } else if (isFour[i]) {
                ballView.setText(String.valueOf(ballRuns[i]));
                ballView.setBackgroundResource(R.drawable.circle_green);
                ballView.setTextColor(Color.WHITE);
            } else {
                ballView.setText(String.valueOf(ballRuns[i]));
                ballView.setBackgroundResource(R.drawable.circle_white);
                ballView.setTextColor(Color.BLACK);
            }

            ballContainer.addView(ballView);
        }

        overLayout.addView(ballContainer);
        overDetailsContainer.addView(overLayout);

        // Add divider
        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(1)
        );
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
        overDetailsContainer.addView(divider);
    }

    // Method to load match summary data
    public void loadMatchSummaryData(MatchSummaryData data) {
        if (data != null) {
            team1Name = data.getTeam1Name();
            team2Name = data.getTeam2Name();
            matchResult = data.getMatchResult();
            playerOfMatch = data.getPlayerOfMatch();

            // Update UI
            tvMatchResult.setText(matchResult);
            tvTeamsLabel.setText(team1Name + " vs " + team2Name);
            tvPlayerOfMatch.setText(playerOfMatch);
            btnTeam1.setText(team1Name);
            btnTeam2.setText(team2Name);

            // Clear existing over details
            overDetailsContainer.removeAllViews();

            // Add over details
            for (MatchSummaryData.OverDetail overDetail : data.getOverDetails()) {
                addOverDetailFromData(overDetail);
            }
        }
    }

    // Method to add over detail from MatchSummaryData.OverDetail object
    private void addOverDetailFromData(MatchSummaryData.OverDetail overDetail) {
        int ballCount = overDetail.getBalls().size();
        int[] ballRuns = new int[ballCount];
        boolean[] isWicket = new boolean[ballCount];
        boolean[] isSix = new boolean[ballCount];
        boolean[] isFour = new boolean[ballCount];

        for (int i = 0; i < ballCount; i++) {
            MatchSummaryData.Ball ball = overDetail.getBalls().get(i);
            ballRuns[i] = ball.getRuns();
            isWicket[i] = ball.isWicket();
            isSix[i] = ball.isSix();
            isFour[i] = ball.isFour();
        }

        addOverDetail(overDetail.getOverText(), ballRuns, isWicket, isSix, isFour);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
