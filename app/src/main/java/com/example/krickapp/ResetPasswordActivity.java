package com.example.krickapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;
import android.view.View;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Replace 'R.layout.activity_reset_password' with the actual name of your XML layout file
        setContentView(R.layout.activity_reset_password);

        // 1. Get references to the UI elements
        emailInput = findViewById(R.id.input_email);
        Button sendInstructionsButton = findViewById(R.id.button_send_instructions);

        // 2. Set the click listener for the button
        sendInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendInstructions();
            }
        });
    }

    /**
     * Handles validation and submission logic.
     */
    private void attemptSendInstructions() {
        // Clear previous error messages
        emailInput.setError(null);

        // Get the email text and trim whitespace
        String email = emailInput.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address
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
            // There was an error; don't proceed and focus the form field
            focusView.requestFocus();
        } else {
            // Success logic: The email is valid, proceed with simulated sending

            // 1. (Real App): Here you would call an API or background task to send the email

            // 2. Display the "Sent Successfully" pop-up using a Toast
            Toast.makeText(
                    ResetPasswordActivity.this,
                    "Email sent successfully! ✅",
                    Toast.LENGTH_LONG
            ).show();

            // Optional: Clear the input field after success
            emailInput.setText("");
        }
    }
}