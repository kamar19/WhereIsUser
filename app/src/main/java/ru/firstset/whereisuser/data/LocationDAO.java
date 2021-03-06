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
//    @Query("SELECT COUNT(DISTINCT track) as count FROM locationUser")
//    @Query("SELECT  b.ID,(SELECT count(a.reagent)FROM mytb AS a WHERE a.reagent=b.ID ) AS reagentCount \n" +
//            "FROM mytb AS b")
//    @Query("SELECT track, count(*) AS id, time FROM locationUser GROUP BY track");
//    @Query("SELECT track, count(*) AS id, time FROM locationUser GROUP BY track")
    @Query("SELECT * FROM locationUser")
    public List<LocationUser> getAllLocations();

//    from (
//                    select TAGID,count(1) cnt from MATERIAL group by TAGID
//    union all
//    select TAGID,count(1) from GUIDELINE group by TAGID
//    union all
//    select TAGID,count(1) from PROJECT group by TAGID
//  )
//    group by TAGID



    @Query("SELECT * FROM locationUser WHERE track= :track ORDER BY time DESC")
    public List<LocationUser> getLocationByTrack(int track);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocationUser location);

    @Delete
    void delete(LocationUser location);

}

