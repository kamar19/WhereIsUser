package ru.firstset.whereisuser.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewDebug;

import ru.firstset.whereisuser.MyMapFragment;
import ru.firstset.whereisuser.R;

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


    public void saveButtonSaveTrackVisible(Boolean value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putBoolean(BUTTON_KEY, value);
            editorSharedPreferences.apply();
            Log.v("saveButton", value.toString());
        }
    }


    public Boolean checkButtonSaveTrack() {
        Boolean boolValue = false;
//        if (sharedPreferences == null) {
//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
//        }

        boolValue = sharedPreferences.getBoolean(BUTTON_KEY, true);
        Log.v("checkButtonSaveTrack", boolValue.toString());
//        if (boolValue) {
//            button.setText(getText(R.string.button_save_track));
//        } else {
//            button.setText(getText(R.string.button_stop_track));
//        }

        return boolValue;
    }
}
