package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLoginSubmit;
    private TextView tvForgotPassword, tvCreateAccount;
    private ImageView btnBackLogin;

    // Declare a FirebaseAuth instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etEmailLogin = findViewById(R.id.et_email_login);
        etPasswordLogin = findViewById(R.id.et_password_login);
        btnLoginSubmit = findViewById(R.id.btn_login_submit);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        btnBackLogin = findViewById(R.id.btn_back_login);

        // Login button logic
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the login user method
                loginUser();
            }
        });

        // Forgot password
        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(login.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        );

        // Go to Create Account
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, reg_account.class);
            startActivity(intent);
        });

        // Back button
        btnBackLogin.setOnClickListener(v -> finish());
    }

    private void loginUser() {
        String email = etEmailLogin.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return; // Stop the function
        }

        // Sign in with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to your app's main dashboard/activity
                            Intent intent = new Intent(login.this, create_match.class);
                            // Clear the activity stack so the user can't go back to the login screen
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
