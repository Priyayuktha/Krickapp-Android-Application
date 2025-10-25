package com.example.krickapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize UI components
        FloatingActionButton fabAddPicture = findViewById(R.id.fab_add_picture);
        TextInputLayout tilName = findViewById(R.id.til_name);
        TextInputLayout tilEmail = findViewById(R.id.til_email);
        TextInputLayout tilPassword = findViewById(R.id.til_password);
        Button btnLogout = findViewById(R.id.button_logout);
        Button btnDeleteAccount = findViewById(R.id.button_delete_account);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Using the initialized variables to prevent "unused" warnings
        tilName.setHint(R.string.name);
        tilEmail.setHint(R.string.email);
        tilPassword.setHint(R.string.password);

        // Set click listeners with lambdas
        fabAddPicture.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Add new profile picture", Toast.LENGTH_SHORT).show());

        btnLogout.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show());

        btnDeleteAccount.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Delete Account clicked", Toast.LENGTH_SHORT).show());

        // Set bottom navigation listener with a lambda
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Toast.makeText(UserProfileActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                Toast.makeText(UserProfileActivity.this, "Matches clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_create) {
                Toast.makeText(UserProfileActivity.this, "Create clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_live) {
                Toast.makeText(UserProfileActivity.this, "Live clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_more) {
                Toast.makeText(UserProfileActivity.this, "More clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
