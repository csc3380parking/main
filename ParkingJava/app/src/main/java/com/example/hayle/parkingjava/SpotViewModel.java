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

    public void insert (Coor lat, Coor lng) { mRepository.insert(lat, lng); }
}
