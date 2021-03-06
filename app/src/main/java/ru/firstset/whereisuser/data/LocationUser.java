package ru.firstset.whereisuser.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

//@Entity(tableName = "locationUser")
@Entity(tableName = "locationUser", primaryKeys = {"id", "track"})
public class LocationUser {
//    @PrimaryKey
    public int id;
    public Double latitude;
    public Double longitude;
    public String title;
    public int track;
    public String time;

    public LocationUser(int id, Double latitude, Double longitude, String title, int track, String time) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.track = track;
        this.time = time;
    }

}

