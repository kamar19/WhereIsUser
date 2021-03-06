package ru.firstset.whereisuser.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewDebug;

import ru.firstset.whereisuser.MyMapFragment;

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
//        if (sharedPreferences == null) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            String string =  Integer.toString(id);
            editorSharedPreferences.putString("BUTTON_KEY2", string);
            editorSharedPreferences.apply();
//        }
    }
    public int loadIdPoint() {
        String string = sharedPreferences.getString( "POINT_KEY", "1");
        Log.v("loadIdPoint", string);

        return Integer.valueOf(string);
    }

    public void saveIdPoint(int id) {
//        if (sharedPreferences == null) {
        SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
        String string =  Integer.toString(id);
        editorSharedPreferences.putString("POINT_KEY", string);
        editorSharedPreferences.apply();
        Log.v("saveIdPoint", String.valueOf(id));

//        }
    }
}
