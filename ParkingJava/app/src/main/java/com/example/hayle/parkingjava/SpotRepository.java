package com.example.hayle.parkingjava;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


public class SpotRepository {
    private ParkingDao mParkingDao;
    private LiveData<List<Double>> mAllCoordinates;

    SpotRepository(Application application) {
        CoordinateDatabase db = CoordinateDatabase.getDatabase(application);
        mParkingDao = db.parkingDao();
        mAllCoordinates = mParkingDao.getAllCoordinates();
    }

    LiveData<List<Double>> getAllWords() {
        return mAllCoordinates;
    }

    //in order to insert both doubles in ParkingDao
    private static class LatLng {
        double lat;
        double lng;
        //constructor
        LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
    public void insertLat (LatLng lat) {
        new insertAsyncTask(mParkingDao).execute(lat);
    }
    public void insertLng (LatLng lng) {
        new insertAsyncTask(mParkingDao).execute(lng);
    }

    private static class insertAsyncTask extends AsyncTask<LatLng, Void, Void> {

        private ParkingDao mAsyncTaskDao;
        //constructor
        insertAsyncTask(ParkingDao dao) {
            mAsyncTaskDao = dao;
        }
        //method needed for insertAsyncTask
        @Override
        protected Void doInBackground(LatLng... params) {
            double lat = params[0].lat;
            double lng = params[0].lng;
            mAsyncTaskDao.insert(lat, lng);
            return null;
        }
    }
}
