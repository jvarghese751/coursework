package com.example.sportsapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SportsApi {
    @GET("search_all_leagues.php")
    Call<Object> getLeagues(@Query("c") String country, @Query("s") String sport);

    @GET("search_all_teams.php")
    Call<JsonObject> getTeams(@Query("l") String league);
}