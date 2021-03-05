package ru.firstset.whereisuser.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {LocationUser.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationDAO getLocationDao();
//
//    public LocationDatabase instance;
//
//    public LocationDatabase createDatabaseInstance(Context applicationContext) {
//        Log.v("createDatabase", "0");
//
////        if (instance == null) {
//            Log.v("createDatabase", "1");
//
//            instance = Room.databaseBuilder(
//                    applicationContext,
//                    LocationDatabase.class,
//                    DBContract.DATABASE_NAME
//            )
//                    .fallbackToDestructiveMigration()
//                    .build();
////        }
//        Log.v("createDatabase", instance.toString());
//
//        return instance;
//    }
//
//    @NonNull
//    @Override
//    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//        return null;
//    }
//
//    @NonNull
//    @Override
//    protected InvalidationTracker createInvalidationTracker() {
//        return null;
//    }
//
//    @Override
//    public void clearAllTables() {
//
//    }
}