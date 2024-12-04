package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.res.Configuration;
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
                        new League("4330", "Scottish Premier League", "Soccer", "SPFL", "Scottish football", "Hampden Park", "Glasgow, Scotland", "51866", "https://example.com/scottish.png"),
                        new League("4331", "German Bundesliga", "Soccer", "Bundesliga", "German football", "Allianz Arena", "Munich, Germany", "75000", "https://example.com/bundesliga.png"),
                        new League("4332", "Italian Serie A", "Soccer", "Serie A", "Italian football", "San Siro", "Milan, Italy", "80018", "https://example.com/seriea.png"),
                        new League("4334", "French Ligue 1", "Soccer", "Ligue 1", "French football", "Parc des Princes", "Paris, France", "47929", "https://example.com/ligue1.png"),
                        new League("4335", "Spanish La Liga", "Soccer", "La Liga", "Spanish football", "Santiago Bernabéu", "Madrid, Spain", "81044", "https://example.com/laliga.png"),
                        new League("4336", "Greek Superleague Greece", "Soccer", "", "Greek football", "Karaiskakis Stadium", "Piraeus, Greece", "32115", "https://example.com/greek.png"),
                        new League("4337", "Dutch Eredivisie", "Soccer", "Eredivisie", "Dutch football", "Johan Cruyff Arena", "Amsterdam, Netherlands", "54000", "https://example.com/eredivisie.png"),
                        new League("4338", "Belgian Pro League", "Soccer", "Jupiler Pro League", "Belgian football", "Constant Vanden Stock Stadium", "Brussels, Belgium", "21500", "https://example.com/belgian.png")
                );

                // Insert leagues into database
                db.leagueDao().insertLeagues(leagues);

                runOnUiThread(() -> Toast.makeText(this, "Leagues added successfully!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error adding leagues: " + e.getMessage(), Toast.LENGTH_LONG).show());
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