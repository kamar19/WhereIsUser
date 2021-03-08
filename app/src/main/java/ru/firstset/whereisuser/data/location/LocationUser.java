package ru.firstset.whereisuser.data.location;

import androidx.room.Entity;

@Entity(tableName = "locationUser", primaryKeys = {"track","id"})
public class LocationUser {
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

