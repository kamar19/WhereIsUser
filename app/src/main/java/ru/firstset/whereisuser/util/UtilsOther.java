package ru.firstset.whereisuser.util;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilsOther {

    static SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy kk:mm:ss", Locale.ENGLISH);

    public static String getCurrentDateTimeString() {
        return sdf.format(new Date());
    }

    public static boolean compareLocations(Float lastKnownLatitude, Float lastKnownLongitude, Float currentLatitude, Float currentLongitude) {
        if ((lastKnownLatitude.compareTo(currentLatitude) == 0) &
                (lastKnownLongitude.compareTo(currentLongitude) == 0)) {
            Log.v("compareLocations","true");
            return true;// Если координаты равны
        }
        else {
            Log.v("lastKnownLatitude", String.valueOf(lastKnownLatitude));
            Log.v("currentLatitude", String.valueOf(currentLatitude));
            Log.v("lastKnownLongitude", String.valueOf(lastKnownLongitude));
            Log.v("currentLongitude", String.valueOf(currentLongitude));
            return false;
        }
    }
}
