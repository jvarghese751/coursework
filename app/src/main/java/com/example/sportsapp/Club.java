package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clubs")
public class Club {
    @PrimaryKey
    @NonNull
    public String idTeam; // Unique ID for the team

    public String name;
    public String strTeamShort;
    public String strAlternate;
    public String intFormedYear;
    public String strLeague;
    public String idLeague;
    public String strStadium;
    public String strKeywords;
    public String strStadiumLocation;
    public String intStadiumCapacity;
    public String strWebsite;
    public String strTeamLogo;

    public Club(@NonNull String idTeam, String name, String strTeamShort, String strAlternate,
                String intFormedYear, String strLeague, String idLeague, String strStadium,
                String strKeywords, String strStadiumLocation, String intStadiumCapacity,
                String strWebsite, String strTeamLogo) {
        this.idTeam = idTeam;
        this.name = name;
        this.strTeamShort = strTeamShort;
        this.strAlternate = strAlternate;
        this.intFormedYear = intFormedYear;
        this.strLeague = strLeague;
        this.idLeague = idLeague;
        this.strStadium = strStadium;
        this.strKeywords = strKeywords;
        this.strStadiumLocation = strStadiumLocation;
        this.intStadiumCapacity = intStadiumCapacity;
        this.strWebsite = strWebsite;
        this.strTeamLogo = strTeamLogo;
    }
}