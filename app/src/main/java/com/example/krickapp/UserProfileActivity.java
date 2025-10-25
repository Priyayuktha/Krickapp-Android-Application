package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText etName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components (EditTexts and Buttons)
        FloatingActionButton fabAddPicture = findViewById(R.id.fab_add_picture);

        // Initialize the inner EditText fields (using the new IDs from the XML)
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        Button btnLogout = findViewById(R.id.button_logout);
        Button btnDeleteAccount = findViewById(R.id.button_delete_account);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // Load and display user data
        loadUserProfile();

        // Set click listeners
        fabAddPicture.setOnClickListener(v ->
                Toast.makeText(UserProfileActivity.this, "Add new profile picture functionality TBD", Toast.LENGTH_SHORT).show()
        );

        btnLogout.setOnClickListener(v -> showLogoutDialog());

        // Delete Account - Calls confirmation dialog
        btnDeleteAccount.setOnClickListener(v -> showDeleteConfirmationDialog());

        // Set bottom navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                startActivity(new Intent(UserProfileActivity.this, MatchesListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(UserProfileActivity.this, create_match.class));
                return true;
            } else if (itemId == R.id.navigation_live) {
                Toast.makeText(this, "Live matches coming soon", Toast.LENGTH_SHORT).show();
                return false;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(UserProfileActivity.this, MoreActivity.class));
                return true;
            }
            return false;
        });
    }

    // ------------------------------------------------------------------
    // 1. Load User Profile Data
    // ------------------------------------------------------------------
    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Set Name (use display name if available, otherwise use email prefix)
            String userName = user.getDisplayName();
            if (userName == null || userName.isEmpty()) {
                // Safely extract the part before the '@' in the email
                userName = user.getEmail() != null ? user.getEmail().split("@")[0] : "User";
            }
            etName.setText(userName);

            // Set Email
            etEmail.setText(user.getEmail());

            // Set Password (USE PLACEHOLDER TEXT ONLY)
            etPassword.setText("**********");

        } else {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_LONG).show();
            // Redirect to login if user is null
            startActivity(new Intent(this, login.class));
            finish();
        }
    }

    // ------------------------------------------------------------------
    // 2. Logout Logic
    // ------------------------------------------------------------------
    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    mAuth.signOut();

                    Toast.makeText(UserProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to the Login screen
                    Intent intent = new Intent(UserProfileActivity.this, login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    // ------------------------------------------------------------------
    // 3. Delete Account Logic
    // ------------------------------------------------------------------
    private void showDeleteConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you absolutely sure you want to permanently delete your account? This action cannot be undone.")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    deleteUserAccount();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void deleteUserAccount() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserProfileActivity.this, "Account successfully deleted.", Toast.LENGTH_LONG).show();

                            // Navigate back to the login screen
                            Intent intent = new Intent(UserProfileActivity.this, login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error.";

                            // Check for common security error
                            if (error.contains("requires recent authentication")) {
                                Toast.makeText(UserProfileActivity.this,
                                        "Security Error: Please log out, log back in immediately, and try deleting again.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this,
                                        "Failed to delete account: " + error,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "No user currently logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}