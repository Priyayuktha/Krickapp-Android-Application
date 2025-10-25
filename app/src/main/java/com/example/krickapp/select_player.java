package com.example.krickapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class select_player extends AppCompatActivity {

    private RecyclerView rvPlayers;
    private PlayerAdapter adapter;
    private List<String> players;
    private Button btnAddPlayer, btnContinue;
    private ImageView btnMenu, btnBack;
    private BottomNavigationView bottomNav;
    private String selectedPlayer = null;
    
    // Firebase
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_player);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "guest";

        // Initialize views
        rvPlayers = findViewById(R.id.rvPlayers);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);
        btnContinue = findViewById(R.id.btnContinue);
        btnMenu = findViewById(R.id.btnMenu);
        btnBack = findViewById(R.id.btnBack);
        bottomNav = findViewById(R.id.bottom_nav);

        // Initialize player list
        players = new ArrayList<>();
        
        // Load players from Firebase
        loadPlayersFromFirebase();

        // Setup RecyclerView
        setupRecyclerView();

        // Back button click
        btnBack.setOnClickListener(v -> finish());

        // Add Player button click
        btnAddPlayer.setOnClickListener(v -> showAddPlayerDialog());

        // Continue button click
        btnContinue.setOnClickListener(v -> {
            if (selectedPlayer != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPlayer", selectedPlayer);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // Menu button click
        btnMenu.setOnClickListener(v -> showMenuOptions());

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void loadPlayersFromFirebase() {
        mDatabase.child("users").child(userId).child("players")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        players.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot playerSnapshot : snapshot.getChildren()) {
                                String playerName = playerSnapshot.getValue(String.class);
                                if (playerName != null) {
                                    players.add(playerName);
                                }
                            }
                        }
                        
                        // Add default players if list is empty
                        if (players.isEmpty()) {
                            players.addAll(Arrays.asList(
                                    "Adithya",
                                    "Akash Devadiga",
                                    "Arun Krishna",
                                    "Jatin",
                                    "Kethan Acharya"
                            ));
                            savePlayersToFirebase();
                        }
                        
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(select_player.this, 
                                "Failed to load players: " + error.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        
                        // Load default players on error
                        players.addAll(Arrays.asList(
                                "Adithya",
                                "Akash Devadiga",
                                "Arun Krishna",
                                "Jatin",
                                "Kethan Acharya"
                        ));
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void savePlayersToFirebase() {
        mDatabase.child("users").child(userId).child("players").setValue(players)
                .addOnSuccessListener(aVoid -> {
                    // Success - no need to show message
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save players", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupRecyclerView() {
        adapter = new PlayerAdapter(players);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        rvPlayers.setAdapter(adapter);

        adapter.setOnPlayerClickListener((position, playerName) -> {
            selectedPlayer = playerName;
            btnContinue.setEnabled(true);
            Toast.makeText(this, "Selected: " + playerName, Toast.LENGTH_SHORT).show();
        });

        adapter.setOnPlayerLongClickListener((position, playerName) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Player")
                    .setMessage("Do you want to delete " + playerName + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        players.remove(position);
                        adapter.notifyItemRemoved(position);
                        if (playerName.equals(selectedPlayer)) {
                            selectedPlayer = null;
                            btnContinue.setEnabled(false);
                        }
                        savePlayersToFirebase();
                        Toast.makeText(this, playerName + " deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void showMenuOptions() {
        String[] options = {"Clear Selection", "Remove All Players"};
        
        new AlertDialog.Builder(this)
                .setTitle("Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Clear selection
                        selectedPlayer = null;
                        btnContinue.setEnabled(false);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Selection cleared", Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        // Remove all players
                        new AlertDialog.Builder(this)
                                .setTitle("Confirm")
                                .setMessage("Are you sure you want to remove all players?")
                                .setPositiveButton("Yes", (d, w) -> {
                                    players.clear();
                                    adapter.notifyDataSetChanged();
                                    selectedPlayer = null;
                                    btnContinue.setEnabled(false);
                                    savePlayersToFirebase();
                                    Toast.makeText(this, "All players removed", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                })
                .show();
    }

    private void showAddPlayerDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter player name");
        input.setPadding(50, 30, 50, 30);

        new AlertDialog.Builder(this)
                .setTitle("Add New Player")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String playerName = input.getText().toString().trim();
                    if (!playerName.isEmpty()) {
                        players.add(playerName);
                        adapter.notifyItemInserted(players.size() - 1);
                        rvPlayers.smoothScrollToPosition(players.size() - 1);
                        savePlayersToFirebase();
                        Toast.makeText(this, "Player added: " + playerName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please enter a player name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_matches) {
                Intent intent = new Intent(this, MatchesListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_create) {
                startActivity(new Intent(this, create_match.class));
                return true;
            } else if (itemId == R.id.navigation_live) {
                Intent intent = new Intent(this, MatchesListActivity.class);
                intent.putExtra("filterStatus", "live");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(this, MoreActivity.class));
                return true;
            }
            return false;
        });
    }
}
