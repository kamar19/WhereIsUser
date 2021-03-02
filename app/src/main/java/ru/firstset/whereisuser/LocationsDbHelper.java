package ru.firstset.whereisuser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocationsDbHelper extends SQLiteOpenHelper {
    private final String DATABASE_NAME = "location_database_name.db";
    private final String LOCATION_TABLE_NAME = "location_table_name";
    private final String ID = "id";
    private final String TITLE = "title";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";

    private final int DATABASE_VERSION = 1;

    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE LOCATION_TABLE_NAME (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TITLE + " TEXT," + LATITUDE + " REAL," + LONGITUDE + " REAL )";

    private final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS LOCATION_TABLE_NAME";

    public LocationsDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
