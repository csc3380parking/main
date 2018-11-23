package com.example.heewon.practicedataup;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Coor.class}, version = 1, exportSchema = false)
public abstract class CoordinateDatabase extends RoomDatabase {
    public abstract ParkingDao parkingDao();
    private static volatile CoordinateDatabase INSTANCE;

    static CoordinateDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoordinateDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoordinateDatabase.class, "spot_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ParkingDao mDao;

        PopulateDbAsync(CoordinateDatabase db) {
            mDao = db.parkingDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteCoordinate();
            Coor coor = new Coor(30.27, -89.76);
            mDao.insert(coor, coor);
            coor = new Coor(30.33, -89.93);
            mDao.insert(coor, coor);
            return null;
        }
    }
}
