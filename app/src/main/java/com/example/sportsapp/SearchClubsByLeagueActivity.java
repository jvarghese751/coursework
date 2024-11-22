package com.example.sportsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchClubsByLeagueActivity extends AppCompatActivity {

    private EditText leagueNameInput;
    private LinearLayout clubsContainer;
    private List<Club> fetchedClubs = new ArrayList<>();
    private RequestQueue requestQueue;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clubs_by_league);

        // Initialize UI components
        leagueNameInput = findViewById(R.id.leagueNameInput);
        clubsContainer = findViewById(R.id.clubsContainer);
        Button retrieveClubsBtn = findViewById(R.id.searchButton);
        Button saveToDbButton = findViewById(R.id.saveToDbButton);
        db = AppDatabase.getInstance(getApplicationContext());

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Set up button listeners
        retrieveClubsBtn.setOnClickListener(v -> {
            String leagueName = leagueNameInput.getText().toString().trim();
            if (!leagueName.isEmpty()) {
                fetchClubsFromWebService(leagueName);
            } else {
                Toast.makeText(this, "Please enter a league name", Toast.LENGTH_SHORT).show();
            }
        });

        saveToDbButton.setOnClickListener(v -> {
            if (!fetchedClubs.isEmpty()) {
                saveClubsToDatabase(fetchedClubs);
            } else {
                Toast.makeText(this, "No clubs to save. Perform a search first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchClubsFromWebService(String leagueName) {
        String url = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=" + leagueName.replace(" ", "%20");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray teamsArray = response.optJSONArray("teams");
                        if (teamsArray != null) {
                            fetchedClubs.clear();
                            clubsContainer.removeAllViews(); // Clear previous clubs

                            for (int i = 0; i < teamsArray.length(); i++) {
                                JSONObject teamObject = teamsArray.getJSONObject(i);

                                String idTeam = teamObject.optString("idTeam", "Unknown");
                                String strTeam = teamObject.optString("strTeam", "Unknown");
                                String strLeague = teamObject.optString("strLeague", "Unknown");
                                String strTeamBadge = teamObject.optString("strTeamBadge", null);

                                Club club = new Club(idTeam, strTeam, strLeague, strTeamBadge);
                                fetchedClubs.add(club);

                                // Dynamically add club to the layout
                                addClubToContainer(club);
                            }

                            Toast.makeText(SearchClubsByLeagueActivity.this, "Clubs retrieved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SearchClubsByLeagueActivity.this, "No clubs found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error parsing response: " + e.getMessage(), e);
                        Toast.makeText(SearchClubsByLeagueActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Volley error: " + error.getMessage(), error);
                    Toast.makeText(SearchClubsByLeagueActivity.this, "Failed to fetch data. Please try again.", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the Volley RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void addClubToContainer(Club club) {
        // Create a parent layout for each club
        LinearLayout clubLayout = new LinearLayout(this);
        clubLayout.setOrientation(LinearLayout.HORIZONTAL);
        clubLayout.setPadding(16, 16, 16, 16);
        clubLayout.setGravity(Gravity.CENTER_VERTICAL);
        clubLayout.setBackgroundColor(Color.LTGRAY);
        clubLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Club Logo
        ImageView logo = new ImageView(this);
        logo.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        if (club.strLogo != null && !club.strLogo.isEmpty()) {
            Glide.with(this).load(club.strLogo).into(logo);
        } else {
            logo.setImageResource(R.drawable.placeholder_image);
        }

        // Club Details
        LinearLayout detailsLayout = new LinearLayout(this);
        detailsLayout.setOrientation(LinearLayout.VERTICAL);
        detailsLayout.setPadding(16, 0, 0, 0);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(club.name);
        nameTextView.setTextSize(18);
        nameTextView.setTextColor(Color.BLACK);

        TextView leagueTextView = new TextView(this);
        leagueTextView.setText(club.strLeague);
        leagueTextView.setTextSize(14);
        leagueTextView.setTextColor(Color.DKGRAY);

        detailsLayout.addView(nameTextView);
        detailsLayout.addView(leagueTextView);

        // Add logo and details to the parent layout
        clubLayout.addView(logo);
        clubLayout.addView(detailsLayout);

        // Add the parent layout to the container
        clubsContainer.addView(clubLayout);
    }

    private void saveClubsToDatabase(List<Club> clubs) {
        new Thread(() -> {
            try {
                if (db != null) {
                    db.clubDao().insertClubs(clubs); // Ensure db is not null
                    runOnUiThread(() -> Toast.makeText(this, "Clubs saved to database successfully!", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Database instance is null.", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                Log.e("DB_ERROR", "Error saving clubs: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(this, "Error saving to database: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}