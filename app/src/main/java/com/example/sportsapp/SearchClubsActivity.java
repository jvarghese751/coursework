package com.example.sportsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SearchClubsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchInput;
    private AppDatabase db;
    private List<Club> clubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clubs);

        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        Button searchButton = findViewById(R.id.searchButton);

        // Initialize database
        db = AppDatabase.getInstance(getApplicationContext());

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                searchClubs(query);
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchClubs(String query) {
        new Thread(() -> {
            try {
                // Add wildcard characters for SQL LIKE query
                String wildcardQuery = "%" + query + "%";
                Log.d("SEARCH_QUERY", "Searching for clubs with: " + wildcardQuery);

                // Perform the search
                clubs = db.clubDao().searchClubs(wildcardQuery);

                // Log the results
                Log.d("SEARCH_RESULTS", "Found clubs: " + (clubs != null ? clubs.size() : 0));

                // Update the RecyclerView on the main thread
                runOnUiThread(() -> {
                    if (clubs != null && !clubs.isEmpty()) {
                        recyclerView.setAdapter(new ClubAdapter(clubs));
                        Toast.makeText(this, "Clubs found: " + clubs.size(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No clubs found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error searching clubs", e);
                runOnUiThread(() -> Toast.makeText(this, "Error searching for clubs", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}