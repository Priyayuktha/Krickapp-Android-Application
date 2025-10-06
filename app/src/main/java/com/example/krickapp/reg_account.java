package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class reg_account extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth mAuth; // Firebase Authentication instance
    private DatabaseReference mDatabase; // Firebase Realtime Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_account);

        // Initialize Firebase Auth and Database
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        Button btnCreateAccountSubmit = findViewById(R.id.btn_create_account_submit);
        TextView tvLogin = findViewById(R.id.tv_login);
        ImageView btnBack = findViewById(R.id.btn_back);

        // Submit button
        btnCreateAccountSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the registration method
                registerUser();
            }
        });

        // Navigate to Login if already have account
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(reg_account.this, login.class);
                startActivity(intent);
            }
        });

        // Back button
        btnBack.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Basic input validation
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(reg_account.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return; // Stop the function
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(reg_account.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return; // Stop the function
        }

        // Use Firebase to create the user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, now save the user's additional data
                            String userId = mAuth.getCurrentUser().getUid();
                            saveUserData(userId, fullName, email);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(reg_account.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveUserData(String userId, String fullName, String email) {
        // Create a User object or a HashMap to store the data
        User user = new User(fullName, email);

        // Save data to Realtime Database under a "users" node with the userId as key
        mDatabase.child("users").child(userId).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(reg_account.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                            // Navigate to Login screen
                            Intent intent = new Intent(reg_account.this, login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(reg_account.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Helper class to structure user data
    public static class User {
        public String fullName;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String fullName, String email) {
            this.fullName = fullName;
            this.email = email;
        }
    }
}
