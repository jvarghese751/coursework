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

        db = AppDatabase.getInstance(getApplicationContext());

        Button addLeaguesToDbBtn = findViewById(R.id.addLeaguesToDbBtn);

        addLeaguesToDbBtn.setOnClickListener(v -> addLeaguesToDatabase());
    }

    private void addLeaguesToDatabase() {
        new Thread(() -> {
            try {
                // Define leagues
                List<League> leagues = Arrays.asList(
                        new League("4330", "Scottish Premier League", "Soccer", "SPFL", "https://example.com/scottish.png"),
                        new League("4331", "German Bundesliga", "Soccer", "Bundesliga", "https://example.com/bundesliga.png"),
                        new League("4332", "Italian Serie A", "Soccer", "Serie A", "https://example.com/seriea.png"),
                        new League("4334", "French Ligue 1", "Soccer", "Ligue 1", "https://example.com/ligue1.png"),
                        new League("4335", "Spanish La Liga", "Soccer", "La Liga", "https://example.com/laliga.png"),
                        new League("4336", "Greek Superleague Greece", "Soccer", "", "https://example.com/greek.png"),
                        new League("4337", "Dutch Eredivisie", "Soccer", "Eredivisie", "https://example.com/eredivisie.png"),
                        new League("4338", "Belgian Pro League", "Soccer", "Jupiler Pro League", "https://example.com/belgian.png")
                );

                // Insert leagues into database
                db.leagueDao().insertLeagues(leagues);

                runOnUiThread(() -> Toast.makeText(this, "Leagues added successfully!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error adding leagues: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}