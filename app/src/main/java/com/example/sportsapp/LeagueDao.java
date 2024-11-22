package com.example.sportsapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
public interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLeagues(List<League> leagues);

    @Query("SELECT * FROM leagues")
    List<League> getAllLeagues();

    @Query("SELECT * FROM leagues WHERE name LIKE :query")
    List<League> searchLeagues(String query);
}