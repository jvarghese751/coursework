package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leagues")
public class League {
    @PrimaryKey
    @NonNull
    public String idLeague;

    public String strLeague;
    public String strSport;
    public String strLeagueAlternate;

    public League(String idLeague, String strLeague, String strSport, String strLeagueAlternate) {
        this.idLeague = idLeague;
        this.strLeague = strLeague;
        this.strSport = strSport;
        this.strLeagueAlternate = strLeagueAlternate;
    }
}