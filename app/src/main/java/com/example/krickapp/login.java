package com.example.krickapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLoginSubmit;
    private TextView tvForgotPassword, tvCreateAccount;
    private ImageView btnBackLogin;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog; // Loading indicator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etEmailLogin = findViewById(R.id.et_email_login);
        etPasswordLogin = findViewById(R.id.et_password_login);
        btnLoginSubmit = findViewById(R.id.btn_login_submit);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        btnBackLogin = findViewById(R.id.btn_back_login);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        // Login button
        btnLoginSubmit.setOnClickListener(v -> loginUser());

        // Forgot password
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        // Create account
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
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(login.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(login.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressDialog.show();

        // Sign in with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
