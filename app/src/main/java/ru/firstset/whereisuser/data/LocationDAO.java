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

    @Query("SELECT * FROM locationUser WHERE track= :track ORDER BY time DESC")
    public List<LocationUser> getLocationByTrack(int track );

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocationUser location);

    @Delete
    void delete(LocationUser location);

}
