package com.example.sportsapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClubs(List<Club> clubs);

    @Query("SELECT * FROM clubs")
    List<Club> getAllClubs();

    @Query("SELECT * FROM clubs WHERE name LIKE :query")
    List<Club> searchClubs(String query);
}