# KrickApp - Quick Navigation Reference

## Activity Links (Intent Navigation)

### MainActivity.java
```java
// Create Account button → reg_account.class
Intent intent = new Intent(MainActivity.this, reg_account.class);

// Login button → login.class
Intent intent = new Intent(MainActivity.this, login.class);
```

### login.java
```java
// After successful login → DashboardActivity.class
Intent intent = new Intent(login.this, DashboardActivity.class);

// Forgot Password link → ResetPasswordActivity.class
Intent intent = new Intent(login.this, ResetPasswordActivity.class);

// Create Account link → reg_account.class
Intent intent = new Intent(login.this, reg_account.class);
```

### reg_account.java
```java
// After successful registration → MainActivity.class
startActivity(new Intent(reg_account.this, MainActivity.class));

// Login link → login.class
startActivity(new Intent(reg_account.this, login.class));
```

### ResetPasswordActivity.java
```java
// Back to Login button → login.class
Intent intent = new Intent(ResetPasswordActivity.this, login.class);
```

### DashboardActivity.java
```java
// Bottom Nav - Live/Create → create_match.class
startActivity(new Intent(DashboardActivity.this, create_match.class));

// Other navigation items: To be implemented
// - nav_matches → MatchesListActivity (not yet created)
// - nav_more → SettingsActivity (not yet created)
```

### create_match.java
```java
// Next button (after filling match details) → TeamSelectionActivity.class
Intent intent = new Intent(create_match.this, TeamSelectionActivity.class);
// Passes: matchName, venue, date, time, matchType
```

### TeamSelectionActivity.java
```java
// Automatically navigates to → reset_password.class (Team Details)
Intent teamIntent = new Intent(TeamSelectionActivity.this, reset_password.class);
// Passes: matchName, venue, date, time, matchType
```

### reset_password.java (Team Details)
```java
// After saving both teams → finish() and return to previous screen
// TODO: Should navigate to match summary or dashboard
```

## Layout to Activity Mapping

| Layout File | Java Activity | Purpose |
|-------------|---------------|---------|
| main.xml | MainActivity.java | Launch screen with login/register options |
| login.xml | login.java | User login with email/password |
| reg_account.xml | reg_account.java | New user registration |
| activity_reset_password.xml | ResetPasswordActivity.java | Password reset via email |
| dashboard.xml | DashboardActivity.java | Main home screen after login |
| create_match.xml | create_match.java | Create new match form |
| team_details.xml | reset_password.java | Enter teams and players |
| create_pass.xml | Not used | Reserved for future use |

## Bottom Navigation IDs (in dashboard.xml)

Located in: `app/src/main/res/menu/bottom_nav_menu.xml`

```xml
nav_home        → Dashboard (current screen)
nav_matches     → Matches List (to be implemented)
nav_placeholder → Disabled spacer
nav_live        → Create Match / Live Scoring
nav_more        → More options / Settings
```

## Common Data Flow

### Match Creation Flow:
1. **create_match.java** collects:
   - matchName (String)
   - venue (String)
   - date (String)
   - time (String)
   - matchType (String)

2. **TeamSelectionActivity.java** receives and passes forward all above

3. **reset_password.java** receives match data and collects:
   - team1Name (String)
   - team1Players (String[11])
   - team2Name (String)
   - team2Players (String[11])

### User Registration Flow:
1. **reg_account.java** collects:
   - fullName (String)
   - email (String)
   - password (String)
   - confirmPassword (String)

2. Creates Firebase Auth user
3. Saves user data to Firebase Realtime Database under `users/{userId}`

### User Login Flow:
1. **login.java** collects:
   - email (String)
   - password (String)

2. Authenticates with Firebase Auth
3. On success → DashboardActivity
4. On failure → Error toast message

## Firebase Auth Current User

Access the currently logged-in user anywhere:
```java
FirebaseAuth mAuth = FirebaseAuth.getInstance();
FirebaseUser currentUser = mAuth.getCurrentUser();

if (currentUser != null) {
    String userId = currentUser.getUid();
    String email = currentUser.getEmail();
}
```

## Important Notes

1. **reset_password.java is MISNAMED**
   - Despite the name, this activity handles team details entry
   - The actual password reset is in ResetPasswordActivity.java
   - Consider renaming to TeamDetailsActivity.java in future

2. **TeamSelectionActivity** is currently a pass-through
   - Could be removed and create_match could directly call reset_password
   - OR could be enhanced to show a team selection/creation screen

3. **Data Persistence**
   - Currently, match and team data are NOT saved to Firebase
   - Need to implement Firebase Database write operations
   - Suggested location: reset_password.java save button

4. **Back Button Behavior**
   - DashboardActivity prevents back navigation (moveTaskToBack)
   - This prevents users from accidentally going back to login
