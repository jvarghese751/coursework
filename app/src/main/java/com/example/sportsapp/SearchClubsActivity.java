package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SearchClubsActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView recyclerView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clubs);

        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button searchButton = findViewById(R.id.searchButton);
        db = AppDatabase.getInstance(getApplicationContext());

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchInput.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                searchClubs(searchQuery); // Call the method to search clubs
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchClubs(String query) {
        new Thread(() -> {
            try {
                String wildcardQuery = "%" + query + "%"; // Use wildcards for SQL LIKE
                List<Club> clubs = db.clubDao().searchClubs(wildcardQuery); // Fetch results from the database

                runOnUiThread(() -> {
                    if (clubs != null && !clubs.isEmpty()) {
                        recyclerView.setAdapter(new ClubAdapter(clubs)); // Populate RecyclerView with results
                        Toast.makeText(this, "Found " + clubs.size() + " clubs.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No clubs found matching the search query.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error searching clubs", e);
                runOnUiThread(() -> Toast.makeText(this, "Error searching for clubs.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Check the orientation and adjust UI if needed
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Handle landscape orientation
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Handle portrait orientation
        }
    }
}