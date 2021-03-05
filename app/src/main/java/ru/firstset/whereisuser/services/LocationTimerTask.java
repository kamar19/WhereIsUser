package ru.firstset.whereisuser.services;

import android.location.Location;
import android.util.Log;

import java.util.TimerTask;

import ru.firstset.whereisuser.location.LocationListenerMap;

public class LocationTimerTask extends TimerTask {
    private static int idRud=0;
    LocationListenerMap locationListenerMap;

        @Override
        public void run() {
            // Получаем локацию, возможно сравниваем с предыдущей и записываем если новая в БД
            Location location = locationListenerMap.getLocation();
            location.getLatitude();
//            getDeviceLocation();
            idRud++;
            Log.v("run()", "Получаем локацию - " +idRud);

        }
}
