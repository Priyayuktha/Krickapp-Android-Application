package com.example.krickapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button b2l;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Replace 'R.layout.activity_reset_password' with the actual name of your XML layout file
        setContentView(R.layout.activity_reset_password);

        // 1. Get references to the UI elements
        emailInput = findViewById(R.id.input_email);
        b2l=findViewById(R.id.button);

        Button sendInstructionsButton = findViewById(R.id.button_send_instructions);

        // 2. Set the click listener for the button
        sendInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendInstructions();
            }
        });

       b2l.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(ResetPasswordActivity.this, login.class);
               startActivity(intent);
           }
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
            // ✅ Use FirebaseAuth to send password reset email
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    ResetPasswordActivity.this,
                                    "Reset instructions sent to " + email,
                                    Toast.LENGTH_LONG
                            ).show();
                            emailInput.setText("");
                        } else {
                            Toast.makeText(
                                    ResetPasswordActivity.this,
                                    "Failed to send reset email. Please check if the email exists.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        }
    }
}