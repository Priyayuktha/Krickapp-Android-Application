package com.example.krickapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnCreateAccount = findViewById(R.id.btn_create_account);
        Button btnLogin = findViewById(R.id.btn_login);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is a placeholder for your navigation logic.
                Toast.makeText(MainActivity.this, "Create Account Clicked", Toast.LENGTH_SHORT).show();
                // You can replace the line above with an Intent to start a new Activity:
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Login Clicked", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });
    }
}