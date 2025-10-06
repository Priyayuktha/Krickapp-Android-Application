package com.example.krickapp;



import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.krickapp.PlayerAdapter;



import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class select_player extends AppCompatActivity {


    RecyclerView rvPlayers;
    PlayerAdapter adapter;
    List<String> players;
    FloatingActionButton fab;
    BottomNavigationView bottomNav;
    AppCompatButton btnAddPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_player);

        rvPlayers = findViewById(R.id.rvPlayers);
        fab = findViewById(R.id.fab_create);
        bottomNav = findViewById(R.id.bottom_nav);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);

        // Initial players
        players = new ArrayList<>(Arrays.asList(
                "Adithya",
                "Akash Devadiga",
                "Arun Krishna",
                "Jatin",
                "Kethan Acharya"
        ));

        adapter = new PlayerAdapter(players);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        rvPlayers.setAdapter(adapter);
        rvPlayers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Item click toast
        adapter.setOnItemClickListener((position, name) ->
                Toast.makeText(this, "Selected: " + name, Toast.LENGTH_SHORT).show()
        );

        // ✅ Add Player button functionality (dialog input)
        btnAddPlayer.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setHint("Enter player name");
            input.setSingleLine(true);

            new AlertDialog.Builder(this)
                    .setTitle("Add Player")
                    .setView(input)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String newPlayer = input.getText().toString().trim();
                        if (!newPlayer.isEmpty()) {
                            players.add(newPlayer);
                            adapter.notifyItemInserted(players.size() - 1);
                            rvPlayers.smoothScrollToPosition(players.size() - 1);
                            Toast.makeText(this, "Added: " + newPlayer, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}

