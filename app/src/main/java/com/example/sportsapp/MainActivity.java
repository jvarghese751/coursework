package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addLeaguesBtn = findViewById(R.id.addLeaguesBtn);
        Button searchClubsByLeagueBtn = findViewById(R.id.searchClubsByLeagueBtn);
        Button searchClubsBtn = findViewById(R.id.searchClubsBtn);

        addLeaguesBtn.setOnClickListener(v -> startActivity(new Intent(this, AddLeaguesActivity.class)));
        searchClubsByLeagueBtn.setOnClickListener(v -> startActivity(new Intent(this, SearchClubsByLeagueActivity.class)));
        searchClubsBtn.setOnClickListener(v -> startActivity(new Intent(this, SearchClubsActivity.class)));
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