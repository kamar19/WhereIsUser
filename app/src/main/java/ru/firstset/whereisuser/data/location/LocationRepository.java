package ru.firstset.whereisuser.data.location;

import android.content.Context;
import androidx.room.Room;
import java.util.List;

public class LocationRepository {
    LocationDatabase locationDatabase;
    Context context;

    public LocationRepository(Context context) {
        this.context = context;
        locationDatabase = Room.databaseBuilder(
                context,
                LocationDatabase.class,
                "locationDb.db"
        )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
    public List<LocationUser> readLocation(int track) {
        return locationDatabase.getLocationDao().getLocationByTrack(track);
    }

    public List<LocationUser> readAllLocations() {
        return locationDatabase.getLocationDao().getAllLocations();
    }

    public void saveLocation(LocationUser locationUser) {
        locationDatabase.getLocationDao().insert(locationUser);
    }

}
