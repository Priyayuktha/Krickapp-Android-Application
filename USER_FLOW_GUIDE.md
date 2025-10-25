# User Flow & Navigation Guide

## 🎯 Navigation Flow Overview

### Main Navigation Structure

```
MainActivity (Login/Register)
    ↓
DashboardActivity (Home)
    ├── Profile Icon → UserProfileActivity
    ├── Match Cards → matchinfo / MatchSummaryActivity
    └── Bottom Nav:
        ├── Home → DashboardActivity
        ├── Matches → MatchesListActivity
        ├── Create → create_match
        ├── Live → MatchesListActivity (Live Tab)
        └── More → MoreActivity
```

## 📱 Detailed User Flows

### 1. **Main Entry Points**

#### A. Dashboard (Home Screen)
- **Entry:** After login
- **Features:**
  - View highlights (featured matches)
  - View recent completed matches
  - View scheduled upcoming matches
- **Navigation:**
  - Click profile icon → User Profile
  - Click completed match card → Match Summary
  - Click scheduled match card → Match Info
  - Bottom nav to all sections

#### B. Matches List
- **Entry:** Bottom nav "Matches" button or "Live" button
- **Features:**
  - Three tabs: Upcoming, Live, Completed
  - Filter matches by status
  - Flip Coin button (visible in Live tab)
- **Click Actions:**
  - Completed match → Match Summary Activity
  - Live match → Score Desk Activity
  - Scheduled match → Match Info Activity

### 2. **Match Creation Flow**

```
create_match (Match Details)
    ↓ Next
TeamDetailsActivity (Team 1 & Team 2)
    ↓ Save
DashboardActivity (Match Created)
```

**Steps:**
1. Click "Create" in bottom nav
2. Enter match details (name, venue, date, time, type)
3. Click "Next"
4. Enter Team 1 details (name + 11 players)
5. Switch to Team 2 tab
6. Enter Team 2 details (name + 11 players)
7. Click "Save"
8. Match saved to Firebase
9. Navigate back to Dashboard

### 3. **Live Match Flow**

```
MatchesListActivity (Select Live Match)
    ↓ Click
scoredesk (Live Scoring)
    ↓ End Match
matchresult (Enter Result)
    ↓ Save
MatchSummaryActivity (View Summary)
```

**Steps:**
1. Go to Matches → Live tab
2. Click on a live match
3. Use scoredesk to record runs, wickets, etc.
4. Click "End Match" when done
5. Enter match result (winner, draw, abandoned)
6. Click "OK" to save
7. Automatically navigate to Match Summary
8. View full match details and over-by-over breakdown

### 4. **Match Summary Flow**

```
Entry Points:
├── MatchesListActivity (Click completed match)
├── DashboardActivity (Click recent match card)
└── matchresult (After saving result)
    ↓
MatchSummaryActivity
    ├── View match result
    ├── View player of the match
    ├── Switch between Team 1 & Team 2 overs
    └── View ball-by-ball details
```

### 5. **More Menu Flow**

```
MoreActivity
    ├── Admin Profile → UserProfileActivity
    ├── About → AboutActivity
    ├── Terms & Conditions → TermsActivity
    └── Support → Email/Contact Info
```

## 🔄 Bottom Navigation Behavior

### Navigation Flags & Back Stack Management

#### Main Screens (Single Instance)
- **DashboardActivity**: `FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP`
- **MatchesListActivity**: `FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP`

These flags ensure:
- Only one instance of the activity exists
- Clicking Home/Matches clears the back stack
- Prevents multiple instances piling up

#### Secondary Screens (Multiple Instances OK)
- **create_match**: Opens new instance
- **MoreActivity**: Opens new instance
- **Match detail screens**: Opens new instance

### Live Button Behavior

Clicking "Live" in bottom nav:
1. Opens MatchesListActivity
2. Automatically switches to "Live" tab
3. Shows only ongoing/live matches
4. Uses intent extra: `intent.putExtra("filterStatus", "live")`

## 📊 Activity Lifecycle Management

### finish() Usage

| From Activity | To Activity | Uses finish() | Reason |
|--------------|-------------|---------------|---------|
| Any | DashboardActivity | ✅ Yes | Clear back stack |
| Any | MatchesListActivity | ✅ Yes | Clear back stack |
| create_match | DashboardActivity | ✅ Yes | Navigate to main |
| matchresult | MatchSummaryActivity | ✅ Yes | Result saved, show summary |
| Any | create_match | ❌ No | Allow back navigation |
| Any | MoreActivity | ❌ No | Allow back navigation |

### Back Button Behavior

- **DashboardActivity**: `moveTaskToBack(true)` - Minimize app
- **Other activities**: Default back behavior - Go to previous screen

## 🎨 Bottom Navigation States

### Selected States by Activity

| Activity | Selected Nav Item |
|----------|------------------|
| DashboardActivity | navigation_home |
| MatchesListActivity | navigation_matches |
| create_match | navigation_create |
| MoreActivity | navigation_more |
| MatchSummaryActivity | navigation_matches |
| scoredesk | None (scoring screen) |
| matchresult | None (result entry) |

## 🔐 Intent Extras Used

### Match Identification
```java
intent.putExtra("matchId", matchId);
```

### Match Summary Data
```java
intent.putExtra("team1Name", "Team 1");
intent.putExtra("team2Name", "Team 2");
intent.putExtra("matchResult", "Team 1 won by 14 runs");
intent.putExtra("playerOfMatch", "Tim David");
```

### Filter Status
```java
intent.putExtra("filterStatus", "live"); // or "completed", "upcoming"
```

### Match Creation Data
```java
intent.putExtra("matchName", matchName);
intent.putExtra("venue", venue);
intent.putExtra("date", date);
intent.putExtra("time", time);
intent.putExtra("matchType", matchType);
```

## 🎯 Key User Paths

### Path 1: Quick Match View
```
Dashboard → Click Recent Match → Match Summary
```

### Path 2: Create New Match
```
Dashboard → Create → Enter Details → Next → Enter Teams → Save → Dashboard
```

### Path 3: Score Live Match
```
Matches → Live Tab → Click Match → Score Desk → Enter Score → End Match → Enter Result → Match Summary
```

### Path 4: Browse Matches
```
Matches → Switch Tabs (Upcoming/Live/Completed) → Click Match → View Details
```

### Path 5: Access Settings
```
Dashboard/Any Screen → More → Select Option (Profile/About/Terms)
```

## ⚠️ Important Navigation Rules

1. **Always use MatchSummaryHelper** for launching Match Summary
   ```java
   MatchSummaryHelper.launchMatchSummaryFromFirebase(activity, matchId);
   ```

2. **Always clear top when navigating to main screens**
   ```java
   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
   ```

3. **Don't finish() when opening secondary screens** (create_match, MoreActivity, etc.)

4. **Use intent extras for filtering** (live matches, match details)

5. **Match Summary should load from Firebase** when possible

## 🐛 Troubleshooting Navigation Issues

### Issue: Multiple activity instances
**Solution**: Use `FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP`

### Issue: Can't go back to previous screen
**Solution**: Don't call `finish()` when opening secondary screens

### Issue: Bottom nav not updating
**Solution**: Call `bottomNav.setSelectedItemId(R.id.navigation_xxx)` in `onCreate()`

### Issue: Live button does nothing
**Solution**: Check intent extra is properly passed to MatchesListActivity

### Issue: Match Summary not loading
**Solution**: Verify matchId is passed and exists in Firebase

## ✅ Navigation Checklist

- [x] All main screens use proper flags
- [x] Bottom navigation works consistently
- [x] Live button filters matches correctly
- [x] Match Summary loads from Firebase
- [x] Create flow saves and navigates properly
- [x] Back button behaves correctly
- [x] No duplicate activity instances
- [x] All click handlers implemented
- [x] Intent extras properly passed
- [x] Error handling in place

---

**Last Updated:** Based on current implementation
**Status:** ✅ All navigation flows fixed and working
