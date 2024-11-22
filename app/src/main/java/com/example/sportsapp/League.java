package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leagues")
public class League {
    @PrimaryKey
    @NonNull
    public String id; // Unique ID for the league

    public String name; // Name of the league
    public String sport; // Type of sport
    public String shortName; // Alternate or short name of the league
    public String strLogo; // URL of the league logo

    public League(@NonNull String id, String name, String sport, String shortName, String strLogo) {
        this.id = id;
        this.name = name;
        this.sport = sport;
        this.shortName = shortName;
        this.strLogo = strLogo;
    }
}