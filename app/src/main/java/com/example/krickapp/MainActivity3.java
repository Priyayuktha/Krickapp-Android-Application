package com.example.krickapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLoginSubmit;
    private TextView tvForgotPassword, tvCreateAccount;
    private ImageView btnBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Initialize
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
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity3.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity3.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    // After login you can redirect to Dashboard/Home Activity
                    // Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // startActivity(intent);
                }
            }
        });

        // Forgot password
        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(MainActivity3.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        );

        // Go to Create Account
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
            startActivity(intent);
        });

        // Back button
        btnBackLogin.setOnClickListener(v -> finish());
    }
}