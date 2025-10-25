# Quick Reference - Firebase Integration

## 🚀 Quick Start

### Launch Match Summary from Any Activity

```java
// Option 1: Load from Firebase (recommended)
MatchSummaryHelper.launchMatchSummaryFromFirebase(this, matchId);

// Option 2: With pre-loaded data
MatchSummaryHelper.launchMatchSummary(
    this,
    matchId,
    "Team 1",
    "Team 2",
    "Team 1 won by 14 runs",
    "Tim David"
);

// Option 3: With complete data object
MatchSummaryData data = new MatchSummaryData(...);
MatchSummaryHelper.launchMatchSummaryWithData(this, data);
```

## 📊 Save Match Data to Firebase

### Save Match Result
```java
// In matchresult.java - already implemented
HashMap<String, Object> summaryMap = new HashMap<>();
summaryMap.put("matchResult", "Team 1 won by 14 runs");
summaryMap.put("winner", "Team 1");
summaryMap.put("playerOfMatch", "Tim David");

mDatabase.child("matches").child(matchId).child("summary")
    .updateChildren(summaryMap);
```

### Save Over Details
```java
// Create over detail
MatchSummaryData.OverDetail over = new MatchSummaryData.OverDetail(
    1,              // overNumber
    "Bowler D",     // bowlerName
    "Athena",       // batsmanName
    15              // totalRuns
);

// Add balls
over.addBall(new MatchSummaryData.Ball(6, false, true, false));  // 6 runs (six)
over.addBall(new MatchSummaryData.Ball(4, false, false, true));  // 4 runs (four)
over.addBall(new MatchSummaryData.Ball(0, true, false, false));  // Wicket
over.addBall(new MatchSummaryData.Ball(1, false, false, false)); // 1 run

// Save to Firebase
MatchSummaryData data = new MatchSummaryData();
data.addOverDetail(over);
saveMatchSummaryToFirebase(data);
```

## 🔍 Load Data from Firebase

### Load Match Summary
```java
DatabaseReference summaryRef = mDatabase.child("matches")
                                       .child(matchId)
                                       .child("summary");

summaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String result = snapshot.child("matchResult").getValue(String.class);
        String player = snapshot.child("playerOfMatch").getValue(String.class);
        // Update UI
    }
    
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(context, "Error: " + error.getMessage(), 
                      Toast.LENGTH_SHORT).show();
    }
});
```

### Load Overs
```java
DatabaseReference oversRef = mDatabase.child("matches")
                                     .child(matchId)
                                     .child("summary")
                                     .child("team1Overs");

oversRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot overSnapshot : snapshot.getChildren()) {
            MatchSummaryData.OverDetail over = 
                overSnapshot.getValue(MatchSummaryData.OverDetail.class);
            // Display over
        }
    }
});
```

## 🎯 Common Use Cases

### 1. From Matches List (Click on completed match)
```java
// In MatchesAdapter.java - already implemented
holder.itemView.setOnClickListener(v -> {
    if (match.getStatus().equals("completed")) {
        MatchSummaryHelper.launchMatchSummaryFromFirebase(
            (AppCompatActivity) context,
            match.getMatchId()
        );
    }
});
```

### 2. After Match Result (Automatic navigation)
```java
// In matchresult.java - already implemented
// After saving result, automatically navigates to summary
MatchSummaryHelper.launchMatchSummary(
    this,
    matchId,
    team1Name,
    team2Name,
    result,
    "To be decided"
);
```

### 3. From Score Desk (After match ends)
```java
// Add to scoredesk.java when match ends
findViewById(R.id.btnEndMatch).setOnClickListener(v -> {
    // Save final scores to Firebase
    // Then navigate to match result or summary
    Intent intent = new Intent(this, matchresult.class);
    intent.putExtra("matchId", matchId);
    intent.putExtra("team1Name", team1Name);
    intent.putExtra("team2Name", team2Name);
    startActivity(intent);
});
```

## 📝 Data Models

### MatchSummaryData
```java
MatchSummaryData data = new MatchSummaryData(
    "match123",           // matchId
    "Team 1",            // team1Name
    "Team 2",            // team2Name
    "Team 1 won by 14",  // matchResult
    "Tim David"          // playerOfMatch
);
```

### OverDetail
```java
MatchSummaryData.OverDetail over = new MatchSummaryData.OverDetail(
    1,           // overNumber
    "D",         // bowlerName
    "Athena",    // batsmanName
    15           // totalRuns
);
```

### Ball
```java
MatchSummaryData.Ball ball = new MatchSummaryData.Ball(
    6,        // runs
    false,    // isWicket
    true,     // isSix
    false     // isFour
);
```

## 🎨 Ball Display Colors

| Runs | Wicket | Six | Four | Color | Background |
|------|--------|-----|------|-------|------------|
| 6    | false  | true| false| White | Blue       |
| 4    | false  | false| true| White | Green      |
| 0    | true   | false| false| White | Red        |
| 0-3,5| false  | false| false| Black | White      |

## ⚠️ Important Notes

1. **Always pass matchId** when launching from completed matches
2. **Check for null** when loading data from Firebase
3. **Handle errors** with appropriate user messages
4. **Update match status** to "completed" after saving result
5. **Use single event listeners** for one-time data fetch
6. **Clear old data** before loading new team's overs

## 🔧 Debugging Tips

### Check if data exists
```java
if (snapshot.exists()) {
    // Data found
} else {
    Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
}
```

### Log Firebase errors
```java
@Override
public void onCancelled(@NonNull DatabaseError error) {
    Log.e("Firebase", "Error: " + error.getMessage());
    Toast.makeText(this, "Failed: " + error.getMessage(), 
                  Toast.LENGTH_SHORT).show();
}
```

### Verify data structure
```java
// Check what's actually in the snapshot
for (DataSnapshot child : snapshot.getChildren()) {
    Log.d("Firebase", "Key: " + child.getKey());
    Log.d("Firebase", "Value: " + child.getValue());
}
```

## ✅ Checklist

Before launching Match Summary:
- [ ] Match has been created in Firebase
- [ ] Match result has been saved
- [ ] Match status is set to "completed"
- [ ] matchId is passed to the activity
- [ ] Internet connection is available
- [ ] Firebase rules allow reading match data

## 📱 Testing Steps

1. Create a match using TeamDetailsActivity
2. Note the matchId from Firebase Console
3. Test loading:
   ```java
   MatchSummaryHelper.launchMatchSummaryFromFirebase(this, matchId);
   ```
4. Verify all data loads correctly
5. Test team switching (Team 1 ↔ Team 2)
6. Test navigation with bottom nav bar

## 🎓 Best Practices

1. **Always use MatchSummaryHelper** instead of direct Intents
2. **Store matchId** in all match-related activities
3. **Cache data locally** for offline viewing (future)
4. **Show loading indicators** during Firebase operations
5. **Handle all error cases** gracefully
6. **Test with real and empty data** scenarios

---

**Need Help?** Check `FIREBASE_INTEGRATION_GUIDE.md` for detailed documentation.
