package com.example.sportsapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClubDao {
    @Query("SELECT * FROM clubs WHERE name LIKE :query COLLATE NOCASE OR strLeague LIKE :query COLLATE NOCASE")
    List<Club> searchClubs(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClubs(List<Club> clubs);
}
