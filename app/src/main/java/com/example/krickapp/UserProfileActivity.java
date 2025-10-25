package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText; // Corrected import
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    // 1. Declare Firebase Auth
    private FirebaseAuth mAuth;
    private TextInputEditText etName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        FloatingActionButton fabAddPicture = findViewById(R.id.fab_add_picture);

        // 2. Initialize the inner EditText fields (using the new IDs)
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        Button btnLogout = findViewById(R.id.button_logout);
        Button btnDeleteAccount = findViewById(R.id.button_delete_account);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // Load and display user data
        loadUserProfile();

        // Set click listeners
        fabAddPicture.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Add new profile picture", Toast.LENGTH_SHORT).show());

        // 3. Implement actual Logout logic
        btnLogout.setOnClickListener(v -> showLogoutDialog());

        btnDeleteAccount.setOnClickListener(v -> {
            // Placeholder for Delete Account logic (Needs confirmation dialog)
            Toast.makeText(UserProfileActivity.this, "Delete Account functionality TBD", Toast.LENGTH_SHORT).show();
        });

        // Set bottom navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            // Note: You should navigate to real activities here, not just show Toasts
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Example navigation back to Dashboard
                startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                // Navigate to Matches List/Schedule
                startActivity(new Intent(UserProfileActivity.this, MatchesListActivity.class));
                return true;
            }
            // ... handle other nav items
            return false;
        });
    }

    // Helper method to load data from Firebase Auth
    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Set Name (use display name if available, otherwise use email prefix)
            String userName = user.getDisplayName();
            if (userName == null || userName.isEmpty()) {
                userName = user.getEmail().split("@")[0]; // Simple email prefix as name
            }
            etName.setText(userName);

            // Set Email
            etEmail.setText(user.getEmail());

            // Set Password (ONLY use placeholder text for security)
            etPassword.setText("**********");

        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_LONG).show();
            // Redirect to login if user is null
            startActivity(new Intent(this, login.class));
            finish();
        }
    }

    // Method to show confirmation dialog and handle logout
    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    mAuth.signOut();

                    Toast.makeText(UserProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to the Login/Main screen
                    Intent intent = new Intent(UserProfileActivity.this, login.class); // Use your main entry activity if different
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