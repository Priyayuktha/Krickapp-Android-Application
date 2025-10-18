# KrickApp - Testing Checklist

## ✅ Code Completion Status

### Activities Linked in AndroidManifest.xml
- [x] MainActivity
- [x] reg_account
- [x] login
- [x] ResetPasswordActivity
- [x] create_match
- [x] reset_password (Team Details)
- [x] DashboardActivity
- [x] TeamSelectionActivity

### Navigation Flow Fixed
- [x] MainActivity → login/reg_account
- [x] login → DashboardActivity (after successful login)
- [x] login → ResetPasswordActivity (forgot password link)
- [x] reg_account → login (login link)
- [x] DashboardActivity → create_match (via nav_live)
- [x] create_match → TeamSelectionActivity
- [x] TeamSelectionActivity → reset_password (team details)

### Code Issues Resolved
- [x] Missing activity declarations in AndroidManifest
- [x] Incorrect navigation (create_match was going to wrong activity)
- [x] Login redirecting to wrong screen
- [x] Team toggle functionality implemented
- [x] Data persistence between team switches
- [x] Button color feedback for team selection

## Testing Steps

### 1. Test User Registration Flow
```
1. Launch app
2. Click "Create Account"
3. Fill in:
   - Full Name: Test User
   - Email: test@example.com
   - Password: test123
   - Confirm Password: test123
4. Click "Create Account"
Expected: Success message, navigate to MainActivity
```

### 2. Test Login Flow
```
1. Click "Login"
2. Enter credentials:
   - Email: test@example.com
   - Password: test123
3. Click "Login"
Expected: Success message, navigate to DashboardActivity
```

### 3. Test Password Reset Flow
```
1. On login screen, click "Forgot Password?"
2. Enter email: test@example.com
3. Click "Send Instructions"
Expected: Email sent confirmation (check email for reset link)
4. Click back arrow
Expected: Return to login screen
```

### 4. Test Match Creation Flow
```
1. On Dashboard, click "Live" tab (bottom navigation)
2. Fill in match details:
   - Match Name: Test Match
   - Venue: Main Ground
   - Date: Select from date picker
   - Time: Select from time picker
   - Match Type: T20
3. Click "Next"
Expected: Navigate to Team Details screen
```

### 5. Test Team Entry Flow
```
1. On Team Details screen, verify "Team 1" is selected (yellow button)
2. Fill in Team 1:
   - Team Name: Team Alpha
   - Players 1-11: Enter player names
3. Click "Team 2" button
Expected: Button turns yellow, form clears
4. Fill in Team 2:
   - Team Name: Team Beta
   - Players 1-11: Enter player names
5. Click "Team 1" button
Expected: Team 1 data reappears, Team 1 button turns yellow
6. Click "Save"
Expected: Success message, return to Dashboard (after Firebase implementation)
```

### 6. Test Bottom Navigation
```
On Dashboard:
1. Click "Home" - Should stay on dashboard
2. Click "Matches" - Should show toast (not implemented)
3. Click "Live" - Should open Create Match
4. Click "More" - Should show toast (not implemented)
```

### 7. Test Back Button Behavior
```
1. From Dashboard, press device back button
Expected: App minimizes (doesn't go back to login)

2. From Create Match, press back button
Expected: Return to Dashboard

3. From Team Details, press back button
Expected: Return to Create Match screen
```

## Validation Tests

### Input Validation - Registration
- [ ] Empty fields show error
- [ ] Invalid email format shows error
- [ ] Password < 6 characters shows error
- [ ] Password mismatch shows error

### Input Validation - Login
- [ ] Empty fields show error
- [ ] Invalid email format shows error
- [ ] Wrong credentials show error

### Input Validation - Create Match
- [ ] Empty fields show error
- [ ] Date picker works correctly
- [ ] Time picker works correctly

### Input Validation - Team Details
- [ ] Cannot save without team names
- [ ] Cannot save without all 11 players for both teams
- [ ] Team switching preserves data
- [ ] Button colors update correctly

## Known Limitations (To Fix)

### High Priority
1. **No Firebase save for matches** - Data is validated but not saved
   - Fix: Implement saveMatchToFirebase() method
   - See: FIREBASE_INTEGRATION_TODO.md

2. **No matches list screen** - Can create matches but can't view them
   - Fix: Create MatchesListActivity
   - See: FIREBASE_INTEGRATION_TODO.md

3. **No match details screen** - Can't view individual match information
   - Fix: Create MatchDetailsActivity

### Medium Priority
4. **TeamSelectionActivity is redundant** - Just passes data through
   - Fix: Remove it and link create_match directly to reset_password
   - OR enhance it with actual team selection UI

5. **File naming confusion**
   - reset_password.java handles team details, not password reset
   - Consider renaming to TeamDetailsActivity.java

6. **No logout functionality**
   - Add logout option in "More" menu

### Low Priority
7. **No profile screen** - Profile icon does nothing
8. **No settings screen** - More menu not implemented
9. **No loading indicators** - Should add ProgressDialog for Firebase operations
10. **No network error handling** - Should check connectivity before Firebase calls

## Firebase Configuration Check

### Before Testing
1. Verify `google-services.json` is in `app/` directory
2. Check Firebase project is configured:
   - Authentication enabled (Email/Password)
   - Realtime Database created
   - Database rules allow read/write for authenticated users

### Current Firebase Status
- [x] google-services.json exists
- [x] Firebase Auth dependency added
- [x] Firebase Database dependency added
- [x] FirebaseApp.initializeApp() called in reg_account
- [ ] Database save operations implemented (partially - only user data)
- [ ] Database read operations implemented (not yet)

## Build and Run

### Build Commands
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Build and install
./gradlew clean assembleDebug installDebug
```

### Expected Build Output
- No compilation errors
- No lint warnings (critical)
- APK size: ~5-10 MB (estimated)

## Success Criteria

App is ready for basic use when:
- [x] All activities are declared and linked
- [x] Navigation flow works end-to-end
- [x] User can register and login
- [x] User can create match details
- [x] User can enter team details
- [ ] Match data is saved to Firebase ⚠️ TODO
- [ ] Matches can be viewed in a list ⚠️ TODO
- [ ] No critical crashes or errors

## Next Development Phase

After completing the above:
1. Implement Firebase match saving
2. Create matches list with RecyclerView
3. Add match details screen
4. Implement live scoring functionality
5. Add toss management
6. Add match highlights feature
7. Implement user profile and settings
