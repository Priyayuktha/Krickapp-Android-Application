package com.example.krickapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class matchinfo extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchinfo);

        TextView tvTopBar = findViewById(R.id.tvTopBar);
        tvTopBar.setOnClickListener(v -> finish());


        // Setup rows with icons + text
        setupRow(R.id.rowMatchType, R.drawable.matchtype, "Match Type", "T20");
        setupRow(R.id.rowDate, R.drawable.date, "Date", "28 August 2025");
        setupRow(R.id.rowTime, R.drawable.time, "Time", "6:00 PM (IST)");
        setupRow(R.id.rowVenue, R.drawable.venue, "Venue", "Green Park Stadium");
        setupRow(R.id.rowCity, R.drawable.city, "City", "Kanpur");
        setupRow(R.id.rowToss, R.drawable.toos, "Toss Win", "Team A choose Batting");
        setupRow(R.id.rowHost, R.drawable.host, "Host", "Local Cricket Association");
    }

    private void setupRow(int rowId, int iconRes, String label, String value) {
        LinearLayout row = findViewById(rowId);
        ImageView icon = row.findViewById(R.id.rowIcon);
        TextView lbl = row.findViewById(R.id.rowLabel);
        TextView val = row.findViewById(R.id.rowValue);

        icon.setImageResource(iconRes);
        lbl.setText(label);
        val.setText(value);
    }
}