# Firebase Integration TODO

## Current Status
✅ Firebase Auth - Implemented
✅ Firebase Database - Dependency added
✅ User registration saves to Database
❌ Match data NOT being saved to Database
❌ Team data NOT being saved to Database
❌ No data retrieval implemented

## Required Implementation

### 1. Save Match and Team Data (reset_password.java)

Add this method to `reset_password.java`:

```java
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

// Add these class variables
private DatabaseReference mDatabase;
private String matchName, venue, date, time, matchType;

// In onCreate(), initialize database and get match data
mDatabase = FirebaseDatabase.getInstance().getReference();
matchName = getIntent().getStringExtra("matchName");
venue = getIntent().getStringExtra("venue");
date = getIntent().getStringExtra("date");
time = getIntent().getStringExtra("time");
matchType = getIntent().getStringExtra("matchType");

// Replace the save button logic with this:
private void saveMatchToFirebase() {
    saveCurrentTeamData();
    
    if (!validateAllData()) {
        Toast.makeText(this, "Please complete both team details", Toast.LENGTH_SHORT).show();
        return;
    }
    
    // Get current user
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String creatorId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "unknown";
    
    // Create unique match ID
    String matchId = mDatabase.child("matches").push().getKey();
    
    // Create match object
    Map<String, Object> match = new HashMap<>();
    match.put("matchName", matchName);
    match.put("venue", venue);
    match.put("date", date);
    match.put("time", time);
    match.put("matchType", matchType);
    match.put("status", "scheduled");
    match.put("createdBy", creatorId);
    match.put("createdAt", System.currentTimeMillis());
    
    // Team 1 data
    Map<String, Object> team1Data = new HashMap<>();
    team1Data.put("name", team1Name);
    Map<String, String> team1PlayersList = new HashMap<>();
    for (int i = 0; i < 11; i++) {
        if (team1Players[i] != null && !team1Players[i].isEmpty()) {
            team1PlayersList.put("player" + (i + 1), team1Players[i]);
        }
    }
    team1Data.put("players", team1PlayersList);
    match.put("team1", team1Data);
    
    // Team 2 data
    Map<String, Object> team2Data = new HashMap<>();
    team2Data.put("name", team2Name);
    Map<String, String> team2PlayersList = new HashMap<>();
    for (int i = 0; i < 11; i++) {
        if (team2Players[i] != null && !team2Players[i].isEmpty()) {
            team2PlayersList.put("player" + (i + 1), team2Players[i]);
        }
    }
    team2Data.put("players", team2PlayersList);
    match.put("team2", team2Data);
    
    // Save to Firebase
    mDatabase.child("matches").child(matchId).setValue(match)
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Match created successfully!", Toast.LENGTH_SHORT).show();
                // Navigate back to dashboard
                Intent intent = new Intent(reset_password.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save match: " + 
                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
}
```

Update the save button listener:
```java
saveBtn.setOnClickListener(v -> {
    saveMatchToFirebase();
});
```

### 2. Create Matches List Activity

Create new file: `MatchesListActivity.java`

```java
package com.example.krickapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);
        
        recyclerView = findViewById(R.id.recycler_matches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        matchesList = new ArrayList<>();
        adapter = new MatchesAdapter(matchesList);
        recyclerView.setAdapter(adapter);
        
        mDatabase = FirebaseDatabase.getInstance().getReference("matches");
        
        loadMatches();
    }
    
    private void loadMatches() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchesList.clear();
                for (DataSnapshot matchSnapshot : snapshot.getChildren()) {
                    Match match = matchSnapshot.getValue(Match.class);
                    if (match != null) {
                        match.setMatchId(matchSnapshot.getKey());
                        matchesList.add(match);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MatchesListActivity.this, 
                    "Failed to load matches", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

### 3. Create Match Model Class

Create new file: `Match.java`

```java
package com.example.krickapp;

public class Match {
    private String matchId;
    private String matchName;
    private String venue;
    private String date;
    private String time;
    private String matchType;
    private String status;
    private String createdBy;
    private long createdAt;
    private Team team1;
    private Team team2;
    
    public Match() {}
    
    // Getters and setters
    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
    
    public String getMatchName() { return matchName; }
    public void setMatchName(String matchName) { this.matchName = matchName; }
    
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    public String getMatchType() { return matchType; }
    public void setMatchType(String matchType) { this.matchType = matchType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public Team getTeam1() { return team1; }
    public void setTeam1(Team team1) { this.team1 = team1; }
    
    public Team getTeam2() { return team2; }
    public void setTeam2(Team team2) { this.team2 = team2; }
    
    public static class Team {
        private String name;
        private Map<String, String> players;
        
        public Team() {}
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Map<String, String> getPlayers() { return players; }
        public void setPlayers(Map<String, String> players) { this.players = players; }
    }
}
```

### 4. Update DashboardActivity Navigation

```java
// In DashboardActivity.java, update the nav_matches case:
} else if (itemId == R.id.nav_matches) {
    startActivity(new Intent(DashboardActivity.this, MatchesListActivity.class));
    return true;
}
```

### 5. Add to AndroidManifest.xml

```xml
<activity android:name=".MatchesListActivity" />
```

## Firebase Database Structure

```
krickapp/
├── users/
│   └── {userId}/
│       ├── fullName: "John Doe"
│       ├── email: "john@example.com"
│       └── createdAt: 1234567890
│
└── matches/
    └── {matchId}/
        ├── matchName: "College Cup 2025"
        ├── venue: "Main Ground"
        ├── date: "18/10/2025"
        ├── time: "14:00"
        ├── matchType: "T20"
        ├── status: "scheduled" | "ongoing" | "completed"
        ├── createdBy: {userId}
        ├── createdAt: 1234567890
        ├── team1/
        │   ├── name: "Team A"
        │   └── players/
        │       ├── player1: "Player Name 1"
        │       ├── player2: "Player Name 2"
        │       └── ... (up to player11)
        ├── team2/
        │   ├── name: "Team B"
        │   └── players/
        │       ├── player1: "Player Name 1"
        │       └── ... (up to player11)
        ├── toss/ (optional - to be added)
        │   ├── won: "team1" | "team2"
        │   └── decision: "bat" | "bowl"
        └── score/ (optional - for live scoring)
            ├── innings1/
            └── innings2/
```

## Firebase Rules (Security)

Update Firebase Realtime Database Rules:

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "matches": {
      ".read": true,
      "$matchId": {
        ".write": "auth != null"
      }
    }
  }
}
```

## RecyclerView Adapter Needed

Create `MatchesAdapter.java` for displaying matches in a list.

## Additional Features to Implement

1. **Pull to refresh** on matches list
2. **Filter matches** by status (scheduled/ongoing/completed)
3. **Search matches** by name or team
4. **Match details screen** when clicking on a match
5. **Edit/Delete match** functionality
6. **Real-time updates** when match status changes
7. **Notifications** for match start/end
