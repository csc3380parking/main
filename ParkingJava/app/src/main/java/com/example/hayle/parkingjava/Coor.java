package com.example.hayle.parkingjava;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"lat", "lng"})
public class Coor {

    @NonNull
    private double lat, lng;

    public Coor(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
    public double getLat(){
        return this.lat;
    }
    public double getLng(){
        return this.lng;
    }
}
