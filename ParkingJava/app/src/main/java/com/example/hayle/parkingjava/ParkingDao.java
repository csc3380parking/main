package com.example.hayle.parkingjava;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

@Dao
public interface ParkingDao {
    @Insert
    void insert(Double lat, Double lng);

    @Delete
    public void deleteCoordinate(parkingSpot ... lat);

    @Query("SELECT * from parkingSpot ORDER BY lat ASC")
    LiveData<List<Double>> getAllCoordinates();
}
