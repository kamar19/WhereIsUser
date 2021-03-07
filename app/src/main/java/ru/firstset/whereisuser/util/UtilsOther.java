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

    public static boolean compareLocations(Double lastKnownLatitude, Double lastKnownLongitude, Double currentLatitude, Double currentLongitude) {
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
//    public static float getFloatRoun3(Double doubleIn){
////        String result = String.format("%.3f",doubleIn);
////        return Float.valueOf(result);
////        double value = 34.777774;
////        double scale = Math.pow(10, 3);
////        Float result = Math.ceil(value * scale) / scale;
//        float result = (float) ((float) Math.round(doubleIn * 100000d) / 100000d);
//        return result;
//    }
}
