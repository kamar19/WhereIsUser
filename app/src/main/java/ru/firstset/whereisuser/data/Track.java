package ru.firstset.whereisuser.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewDebug;

import ru.firstset.whereisuser.MyMapFragment;

public class Track {
    SharedPreferences sharedPreferences;

    public Track(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public int loadIdTrack() {
//        if (sharedPreferences != null) {
        Log.v("BUTTON_KEY",MyMapFragment.BUTTON_KEY);
//        Integer.valueOf(sharedPreferences.getString(
           String string = sharedPreferences.getString( "BUTTON_KEY2", "1");
        Log.v("BUTTON_KEY", string);

        return Integer.valueOf(string);
    }

    public void saveIdTrack(int id) {
//        if (sharedPreferences == null) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            String string =  Integer.toString(id);
            editorSharedPreferences.putString("BUTTON_KEY2", string);
            editorSharedPreferences.apply();
//        }
    }
    public int loadIdPoint() {
        String string = sharedPreferences.getString( "POINT_KEY", "1");
        return Integer.valueOf(string);
    }

    public void saveIdPoint(int id) {
//        if (sharedPreferences == null) {
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        String string =  Integer.toString(id);
        editorSharedPreferences.putString("POINT_KEY", string);
        editorSharedPreferences.apply();
//        }
    }
}
