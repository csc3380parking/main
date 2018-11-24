/*package com.example.hayle.parkingjava;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Coor.class}, version = 1)
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
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}*/
