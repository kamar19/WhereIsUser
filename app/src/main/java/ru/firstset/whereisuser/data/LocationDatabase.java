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

@Database(entities = {LocationUser.class}, version = 2)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationDAO getLocationDao();

}