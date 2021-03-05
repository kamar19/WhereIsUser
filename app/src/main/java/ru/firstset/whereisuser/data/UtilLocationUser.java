package ru.firstset.whereisuser.data;

import android.content.SharedPreferences;

import ru.firstset.whereisuser.MyMapFragment;

public class UtilLocationUser {

//    public UtilLocationUser(int id, Double latitude, Double longitude, String title, int track, String time) {
//        super(id, latitude, longitude, title, track, time);
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.title = title;
//        this.track = track;
//        this.time = time;
//    }

    public Float getCurrentLatitude(SharedPreferences sharedPreferences) {
        return Float.valueOf(sharedPreferences.getString(MyMapFragment.CURRENT_LATITUDE, String.valueOf( MyMapFragment.defaultLocation.latitude)));
    }

    public Float getCurrentLongitude(SharedPreferences sharedPreferences) {
        return Float.valueOf(sharedPreferences.getString(MyMapFragment.CURRENT_LONGITUDE, String.valueOf( MyMapFragment.defaultLocation.longitude)));

    }

    public boolean saveCurrentLocation(SharedPreferences sharedPreferences, Float latitude, Float longitude) {
        if (sharedPreferences == null) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putString(MyMapFragment.CURRENT_LATITUDE, String.valueOf(latitude));
            editorSharedPreferences.putString(MyMapFragment.CURRENT_LONGITUDE, String.valueOf(longitude));
            editorSharedPreferences.apply();
            return true;
        }
        return false;
    }

}
