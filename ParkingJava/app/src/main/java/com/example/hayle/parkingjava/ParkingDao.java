package com.example.hayle.parkingjava;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ParkingDao {
    @Insert
    void insert(Coor lat, Coor lng);

    @Delete
    void deleteCoordinate(Coor ... lat);

    @Query("SELECT * from Coor ORDER BY lat ASC")
    LiveData<List<Coor>> getAllCoordinates();
}
