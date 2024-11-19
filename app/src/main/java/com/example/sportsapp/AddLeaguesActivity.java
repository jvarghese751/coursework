package com.example.sportsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class AddLeaguesActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leagues);

        // Initialize Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "sports_db")
                .fallbackToDestructiveMigration() // Optional: Clears DB if schema changes
                .build();

        Button addLeaguesToDbBtn = findViewById(R.id.addLeaguesToDbBtn);

        addLeaguesToDbBtn.setOnClickListener(v -> addLeaguesToDatabase());
    }

    private void addLeaguesToDatabase() {
        new Thread(() -> {
            try {
                // Define leagues
                List<League> leagues = Arrays.asList(
                        new League("4330", "Scottish Premier League", "Soccer", "SPFL"),
                        new League("4331", "German Bundesliga", "Soccer", "Bundesliga"),
                        new League("4332", "Italian Serie A", "Soccer", "Serie A"),
                        new League("4334", "French Ligue 1", "Soccer", "Ligue 1 Conforama"),
                        new League("4335", "Spanish La Liga", "Soccer", "La Liga")
                );

                // Insert leagues into database
                db.leagueDao().insertLeagues(leagues);
                runOnUiThread(() -> Toast.makeText(this, "Leagues added to database!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error inserting leagues", e);
                runOnUiThread(() -> Toast.makeText(this, "Error adding leagues", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}