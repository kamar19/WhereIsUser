package ru.firstset.whereisuser.data;

import android.location.Location;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM locationUser WHERE id= :id ORDER BY time DESC")
    public LocationUser getLocationById(int id);

//    @Query("SELECT * FROM locationUser ORDER BY track DESC")
    @Query("SELECT track, count(id) AS id, time, title,latitude, longitude FROM locationUser GROUP BY track")
    public List<LocationUser> getAllLocations();

    @Query("SELECT * FROM locationUser WHERE track= :track ORDER BY time DESC")
    public List<LocationUser> getLocationByTrack(int track);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocationUser location);

    @Delete
    void delete(LocationUser location);
 //("DELETE FROM locationUser")

//    @Delete ("DELETE FROM locationUser")
//    void deleteAll();

}

