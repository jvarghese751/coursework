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
            List<Club> clubs = db.clubDao().searchClubs("%" + query + "%");
            runOnUiThread(() -> {
                if (clubs.isEmpty()) {
                    Toast.makeText(this, "No clubs found", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setAdapter(new ClubAdapter(clubs));
                }
            });
        }).start();
    }
}