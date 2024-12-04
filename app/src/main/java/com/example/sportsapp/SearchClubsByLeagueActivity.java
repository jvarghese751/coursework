package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
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
                            clubsContainer.removeAllViews();

                            for (int i = 0; i < teamsArray.length(); i++) {
                                JSONObject teamObject = teamsArray.getJSONObject(i);

                                String idTeam = teamObject.optString("idTeam", "Unknown");
                                String name = teamObject.optString("strTeam", "Unknown");
                                String strTeamShort = teamObject.optString("strTeamShort", "N/A");
                                String strAlternate = teamObject.optString("strAlternate", "N/A");
                                String intFormedYear = teamObject.optString("intFormedYear", "N/A");
                                String strLeague = teamObject.optString("strLeague", "Unknown");
                                String idLeague = teamObject.optString("idLeague", "N/A");
                                String strStadium = teamObject.optString("strStadium", "N/A");
                                String strKeywords = teamObject.optString("strKeywords", "N/A");
                                String strStadiumLocation = teamObject.optString("strStadiumLocation", "N/A");
                                String intStadiumCapacity = teamObject.optString("intStadiumCapacity", "N/A");
                                String strWebsite = teamObject.optString("strWebsite", "N/A");
                                String strTeamLogo = teamObject.optString("strTeamLogo", null);

                                Club club = new Club(idTeam, name, strTeamShort, strAlternate, intFormedYear,
                                        strLeague, idLeague, strStadium, strKeywords, strStadiumLocation,
                                        intStadiumCapacity, strWebsite, strTeamLogo);
                                fetchedClubs.add(club);

                                addClubToContainer(club);
                            }

                            Toast.makeText(this, "Clubs retrieved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "No clubs found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error parsing response: " + e.getMessage(), e);
                        Toast.makeText(this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR", "Volley error: " + error.getMessage(), error);
                    Toast.makeText(this, "Failed to fetch data. Please try again.", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void addClubToContainer(Club club) {
        LinearLayout clubLayout = new LinearLayout(this);
        clubLayout.setOrientation(LinearLayout.VERTICAL);
        clubLayout.setPadding(16, 16, 16, 16);
        clubLayout.setBackgroundColor(Color.LTGRAY);
        clubLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Add club logo
        ImageView logoImageView = new ImageView(this);
        logoImageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        if (club.strTeamLogo != null) {
            Glide.with(this).load(club.strTeamLogo).into(logoImageView);
        } else {
            logoImageView.setImageResource(R.drawable.placeholder_image); // Placeholder
        }
        clubLayout.addView(logoImageView);

        // Add dynamic fields
        clubLayout.addView(createTextView("ID Team: " + club.idTeam));
        clubLayout.addView(createTextView("Name: " + club.name));
        clubLayout.addView(createTextView("Short Name: " + club.strTeamShort));
        clubLayout.addView(createTextView("Alternate Name: " + club.strAlternate));
        clubLayout.addView(createTextView("Formed Year: " + club.intFormedYear));
        clubLayout.addView(createTextView("League: " + club.strLeague));
        clubLayout.addView(createTextView("League ID: " + club.idLeague));
        clubLayout.addView(createTextView("Stadium: " + club.strStadium));
        clubLayout.addView(createTextView("Keywords: " + club.strKeywords));
        clubLayout.addView(createTextView("Stadium Location: " + club.strStadiumLocation));
        clubLayout.addView(createTextView("Stadium Capacity: " + club.intStadiumCapacity));

        // Add website with a clickable link
        TextView websiteTextView = createTextView("Website: " + club.strWebsite);
        websiteTextView.setTextColor(Color.BLUE);
        websiteTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + club.strWebsite));
            startActivity(browserIntent);
        });
        clubLayout.addView(websiteTextView);

        // Add the parent layout to the container
        clubsContainer.addView(clubLayout);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(0, 8, 0, 8);
        return textView;
    }

    private void saveClubsToDatabase(List<Club> clubs) {
        new Thread(() -> {
            db.clubDao().insertClubs(clubs);
            runOnUiThread(() -> Toast.makeText(this, "Clubs saved to database successfully!", Toast.LENGTH_SHORT).show());
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