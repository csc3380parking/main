
package com.example.hayle.parkingjava;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class SpotRepository {
    private ParkingDao mParkingDao;
    private LiveData<List<Coor>> mAllCoordinates;

    SpotRepository(Application application) {
        CoordinateDatabase db = CoordinateDatabase.getDatabase(application);
        mParkingDao = db.parkingDao();
        mAllCoordinates = mParkingDao.getAllCoordinates();
    }

    LiveData<List<Coor>> getAllCoordinates() {
        return mAllCoordinates;
    }
<<<<<<< Updated upstream

    //in order to insert both doubles in ParkingDao
    private static class Coord {
        Coor lat;
        Coor lng;
        //constructor
        Coord(Coor lat, Coor lng) {
=======
    public static class Coord {
        double lat;
        double lng;
        //constructor
        Coord(double lat, double lng) {
>>>>>>> Stashed changes
            this.lat = lat;
            this.lng = lng;
        }
    }
<<<<<<< Updated upstream
    public void insert (Coor lat, Coor lng) {
        Coord params = new Coord(lat, lng);
        insertAsyncTask myTask = new insertAsyncTask(mParkingDao);
        //new insertAsyncTask(mParkingDao).execute(lat, lng);
        myTask.execute(params);
=======
    public void insert(Coord lat, Coord lng){new insertAsyncTask(mParkingDao).execute(lat, lng);}

    public void insertLat (Coord lat) {
        new insertAsyncTask(mParkingDao).execute(lat);
    }
    public void insertLng (Coord lng) {
        new insertAsyncTask(mParkingDao).execute(lng);
>>>>>>> Stashed changes
    }

    private static class insertAsyncTask extends AsyncTask<Coord, Void, Void> {

        private ParkingDao mAsyncTaskDao;
        //constructor
        insertAsyncTask(ParkingDao dao) {
            mAsyncTaskDao = dao;
        }
        //method needed for insertAsyncTask
        @Override
        protected Void doInBackground(final Coord... params) {
<<<<<<< Updated upstream
            Coor lat = params[0].lat;
            Coor lng = params[0].lng;
=======
            double lat = params[0].lat;
            double lng = params[0].lng;
>>>>>>> Stashed changes
            mAsyncTaskDao.insert(lat, lng);
            return null;
        }
    }
}
