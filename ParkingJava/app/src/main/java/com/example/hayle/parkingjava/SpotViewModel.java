package com.example.hayle.parkingjava;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SpotViewModel extends AndroidViewModel {
    private SpotRepository mRepository;
    private LiveData<List<Double>> mAllCoordinates;

    public SpotViewModel (Application application) {
        super(application);
        mRepository = new SpotRepository(application);
        mAllCoordinates = mRepository.getAllWords();
    }
    //hides implementation from UI
    LiveData<List<Double>> getAllWords() { return mAllCoordinates; }

    public void insertLat (LatLng lat) { mRepository.insert(lat); }
}
