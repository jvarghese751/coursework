package com.example.sportsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchClubsByLeagueActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText leagueNameInput;
    private SportsApi sportsApi;
    private List<Club> clubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clubs_by_league);

        // Initialize UI components
        leagueNameInput = findViewById(R.id.leagueNameInput);
        recyclerView = findViewById(R.id.recyclerView);
        Button searchButton = findViewById(R.id.searchButton);

        // Initialize API and data structures
        sportsApi = ApiClient.getRetrofitInstance().create(SportsApi.class);
        clubs = new ArrayList<>();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up search button listener
        searchButton.setOnClickListener(v -> {
            String leagueName = leagueNameInput.getText().toString().trim();
            if (!leagueName.isEmpty()) {
                searchClubsByLeague(leagueName);
            } else {
                Toast.makeText(this, "Please enter a league name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchClubsByLeague(String leagueName) {
        sportsApi.getTeams(leagueName).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonObject = response.body();
                    JsonArray teams = jsonObject.getAsJsonArray("teams");

                    if (teams != null) {
                        clubs.clear();
                        for (JsonElement teamElement : teams) {
                            JsonObject teamObject = teamElement.getAsJsonObject();

                            // Safely get values, using null checks
                            String idTeam = teamObject.has("idTeam") && !teamObject.get("idTeam").isJsonNull()
                                    ? teamObject.get("idTeam").getAsString()
                                    : "Unknown ID";

                            String name = teamObject.has("strTeam") && !teamObject.get("strTeam").isJsonNull()
                                    ? teamObject.get("strTeam").getAsString()
                                    : "Unknown Name";

                            String strLeague = teamObject.has("strLeague") && !teamObject.get("strLeague").isJsonNull()
                                    ? teamObject.get("strLeague").getAsString()
                                    : "Unknown League";

                            String logoUrl = teamObject.has("strTeamBadge") && !teamObject.get("strTeamBadge").isJsonNull()
                                    ? teamObject.get("strTeamBadge").getAsString()
                                    : null; // Handle missing logo

                            // Add the club to the list
                            clubs.add(new Club(idTeam, name, strLeague, logoUrl));
                        }

                        // Update the RecyclerView adapter
                        recyclerView.setAdapter(new ClubAdapter(clubs));
                    } else {
                        Toast.makeText(SearchClubsByLeagueActivity.this, "No teams found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Response failed: Code " + response.code());
                    Toast.makeText(SearchClubsByLeagueActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle failure scenarios
                Log.e("API_FAILURE", "Error: " + t.getMessage(), t);
                Toast.makeText(SearchClubsByLeagueActivity.this, "Network error. Please retry.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}