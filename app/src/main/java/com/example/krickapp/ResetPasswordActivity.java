package com.example.krickapp;

import android.app.ProgressDialog; // Import for ProgressDialog
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button b2l;
    private Button sendInstructionsButton; // Declare button here for better scope

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog; // ProgressDialog declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending reset instructions...");
        progressDialog.setCancelable(false); // User cannot dismiss it easily

        // 1. Get references to the UI elements
        emailInput = findViewById(R.id.input_email);
        b2l = findViewById(R.id.button); // Button to go back to login
        sendInstructionsButton = findViewById(R.id.button_send_instructions);

        // 2. Set the click listener for the main reset button
        sendInstructionsButton.setOnClickListener(v -> attemptSendInstructions());

        // Button to go back to login
        b2l.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, login.class);
            startActivity(intent);
            finish(); // Finish this activity to avoid stacking
        });
    }

    /**
     * Handles validation and submission logic.
     */
    private void attemptSendInstructions() {
        emailInput.setError(null);
        String email = emailInput.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Validation logic (cleaner structure)
        if (email.isEmpty()) {
            emailInput.setError("This field is required");
            focusView = emailInput;
            cancel = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            focusView = emailInput;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show progress indicator before sending request
            progressDialog.show();

            // Use the initialized mAuth instance
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        // Dismiss the dialog once the task is complete
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    ResetPasswordActivity.this,
                                    "Reset instructions sent to " + email,
                                    Toast.LENGTH_LONG
                            ).show();
                            // Optional: Clear the email input after successful send
                            emailInput.setText("");
                        } else {
                            // Provide more informative feedback from the error message
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error.";
                            Toast.makeText(
                                    ResetPasswordActivity.this,
                                    "Failed to send reset email: " + errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        }
    }
}