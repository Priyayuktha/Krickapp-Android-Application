package com.example.krickapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, go directly to Dashboard
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return; // Exit onCreate to prevent showing login screen
        }
        
        setContentView(R.layout.main);

        Button btnCreateAccount = findViewById(R.id.btn_create_account);
        Button btnLogin = findViewById(R.id.btn_login);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is a placeholder for your navigation logic.
                Toast.makeText(MainActivity.this, "Create Account Clicked", Toast.LENGTH_SHORT).show();
                // You can replace the line above with an Intent to start a new Activity:
                Intent intent = new Intent(MainActivity.this, reg_account.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Login Clicked", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
    }
}