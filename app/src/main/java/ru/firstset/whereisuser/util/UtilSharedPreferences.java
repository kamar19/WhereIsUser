package ru.firstset.whereisuser.util;

import android.content.SharedPreferences;

import ru.firstset.whereisuser.MyMapFragment;

import static ru.firstset.whereisuser.MyMapFragment.BUTTON_KEY;

public class UtilSharedPreferences {
    SharedPreferences sharedPreferences;

    public UtilSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int loadIdTrack() {
           String string = sharedPreferences.getString( "BUTTON_KEY2", "1");
        return Integer.valueOf(string);
    }

    public void saveIdTrack(int id) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putString("BUTTON_KEY2", Integer.toString(id));
            editorSharedPreferences.apply();
    }
    public int loadIdPoint() {
        return Integer.valueOf(sharedPreferences.getString( "POINT_KEY", "1"));
    }

    public void saveIdPoint(int id) {
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        String string =  Integer.toString(id);
        editorSharedPreferences.putString("POINT_KEY", string);
        editorSharedPreferences.apply();
    }

    public void saveButtonSaveTrackVisible(Boolean value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putBoolean(BUTTON_KEY, value);
            editorSharedPreferences.apply();
        }
    }

    public Boolean checkButtonSaveTrack() {
        return sharedPreferences.getBoolean(BUTTON_KEY, true);
    }

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
