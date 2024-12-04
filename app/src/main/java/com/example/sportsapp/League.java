package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leagues")
public class League {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String sport;
    public String shortName;
    public String keywords;
    public String stadiumName;
    public String stadiumLocation;
    public String stadiumCapacity;
    public String logo;

    public League(@NonNull String id, String name, String sport, String shortName,
                  String keywords, String stadiumName, String stadiumLocation,
                  String stadiumCapacity, String logo) {
        this.id = id;
        this.name = name;
        this.sport = sport;
        this.shortName = shortName;
        this.keywords = keywords;
        this.stadiumName = stadiumName;
        this.stadiumLocation = stadiumLocation;
        this.stadiumCapacity = stadiumCapacity;
        this.logo = logo;
    }
}