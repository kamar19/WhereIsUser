package ru.firstset.whereisuser.util;

import android.content.SharedPreferences;
import ru.firstset.whereisuser.MyMapFragment;

public class UtilLocationUser {

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
