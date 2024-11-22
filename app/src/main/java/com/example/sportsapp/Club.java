package com.example.sportsapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clubs")
public class Club {
    @PrimaryKey
    @NonNull
    public String id; // Unique ID for the club

    public String name; // Name of the club
    public String strLeague; // League name the club belongs to
    public String strLogo; // URL of the club's logo

    public Club(@NonNull String id, String name, String strLeague, String strLogo) {
        this.id = id;
        this.name = name;
        this.strLeague = strLeague;
        this.strLogo = strLogo;
    }
}