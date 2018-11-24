/*
package com.example.hayle.parkingjava;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class SpotViewModel extends AndroidViewModel {
    private SpotRepository mRepository;
    private LiveData<List<Coor>> mAllCoordinates;

    public SpotViewModel (Application application) {
        super(application);
        mRepository = new SpotRepository(application);
        mAllCoordinates = mRepository.getAllCoordinates();
    }
    //hides implementation from UI
    LiveData<List<Coor>> getAllCoordinates() { return mAllCoordinates; }
<<<<<<< Updated upstream

    public void insert (Coor lat, Coor lng) { mRepository.insert(lat, lng); }
=======

    private static class Coord extends SpotRepository.Coord {
        double lat;
        double lng;
        //constructor
        Coord(double lat, double lng) {
            super(lat, lng);
            this.lat = lat;
            this.lng = lng;
        }
    }
    public void insert (Coord lat, Coord lng) { mRepository.insert(lat, lng); }

>>>>>>> Stashed changes
}*/
