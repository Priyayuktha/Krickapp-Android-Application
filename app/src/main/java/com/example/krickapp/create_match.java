package com.example.krickapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class create_match extends AppCompatActivity {

    EditText etMatchName, etVenue, etDate, etTime, etMatchType;
    Button btnCancel, btnNext;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_match);

        etMatchName = findViewById(R.id.etMatchName);
        etVenue = findViewById(R.id.etVenue);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etMatchType = findViewById(R.id.etMatchType);

        btnCancel = findViewById(R.id.btnCancel);
        btnNext = findViewById(R.id.btnNext);

        // Setup bottom navigation
        bottomNav = findViewById(R.id.bottom_nav);

        // Set Create as selected
        bottomNav.setSelectedItemId(R.id.navigation_create);

        // Handle navigation
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                startActivity(new Intent(this, MatchesListActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_create) {
                // Already on create screen
                return true;
            } else if (itemId == R.id.navigation_live) {
                Toast.makeText(this, "Live matches coming soon", Toast.LENGTH_SHORT).show();
                return false;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(this, MoreActivity.class));
                finish();
                return true;
            }
            return false;
        });

        // Date Picker
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(create_match.this,
                    (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                        // Format date with leading zeros: DD/MM/YYYY
                        String formattedDate = String.format("%02d/%02d/%d", dayOfMonth, (month1 + 1), year1);
                        etDate.setText(formattedDate);
                    }, year, month, day);
            dialog.show();
        });

        // Time Picker
        etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(create_match.this,
                    (TimePicker view, int hourOfDay, int minute1) -> {
                        // Format time with leading zeros
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minute1);
                        etTime.setText(formattedTime);
                    }, hour, minute, true);
            dialog.show();
        });

        // Cancel Button
        btnCancel.setOnClickListener(v -> finish());

        // Next Button
        btnNext.setOnClickListener(v -> {
            String matchName = etMatchName.getText().toString();
            String venue = etVenue.getText().toString();
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            String matchType = etMatchType.getText().toString();

            if (matchName.isEmpty() || venue.isEmpty() || date.isEmpty() || time.isEmpty() || matchType.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Pass data directly to team details activity
                Intent intent = new Intent(create_match.this, TeamDetailsActivity.class);
                intent.putExtra("matchName", matchName);
                intent.putExtra("venue", venue);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("matchType", matchType);
                startActivity(intent);
            }
        });
    }
}

