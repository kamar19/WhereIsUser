package ru.firstset.whereisuser.location;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

import ru.firstset.whereisuser.data.LocationDatabase;
import ru.firstset.whereisuser.data.LocationUser;

public class LocationRepository {
    LocationDatabase locationDatabase;
//    LocationDAO locationDAO;
    Context context;

    public LocationRepository(Context context) {
        this.context = context;
        Log.v("LocationRepository","1");

        locationDatabase = Room.databaseBuilder(
                context,
                LocationDatabase.class,
                "locationDb.db"
        )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
//        }



//        locationDatabase = new LocationDatabase();
//        locationDatabase.createDatabaseInstance(context);

        Log.v("LocationRepository","2");
    }
//    LocationDAO locationDAO= LocationDatabase. ocationDAO;
//    CoroutineContext coroutineContext;

    //    LocationDatabase db =  Room.databaseBuilder(getApplicationContext(),
//            AppDatabase.class, "database").build();
//
//    val localDataStore: MovieDAO = movieDatabase.movieDAO
//
//    LocationDatabase db =  Room.databaseBuilder( context,
//            AppDatabase.class, "database").build();
//


    //    public ArrayList<LocationUser> readLocationFromDb(LocationUser locationUser) {
//        withContext(context, Dispatchers.getIO(), readLocation());
//
//        }
    public List<LocationUser> readLocations(int track) {
        return locationDatabase.getLocationDao().getLocationByTrack(track);
    }

//    public LocationUser readPastLocation() {
//        return MyMapFragment.locationDatabase.locationDao(). getLocation();
//    }


    public void saveLocation(LocationUser locationUser) {
//        Log.v("locationDatabase", locationDatabase.toString());
//        Log.v("instance", locationDatabase.instance.toString());
//        Log.v("locationDao", locationDatabase.instance.locationDao.toString());
        locationDatabase.getLocationDao().insert(locationUser);
//        locationDatabase.instance.locationDao.insert(locationUser);
        Log.v("saveLocation","1");

    }

}
