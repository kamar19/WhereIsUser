package ru.firstset.whereisuser.services;

import android.util.Log;

import java.util.TimerTask;

import ru.firstset.whereisuser.MyMapFragment;
import ru.firstset.whereisuser.location.LocationListenerMap;

import static ru.firstset.whereisuser.MyMapFragment.getUserLocation;

public class LocationTimerTask extends TimerTask {
    private static int idRud=0;
    LocationListenerMap locationListenerMap;

        @Override
        public void run() {
            // Получаем локацию, возможно сравниваем с предыдущей и записываем если новая в БД
//            Location location = locationListenerMap.getLocation();
//            location.getLatitude();
//                  getLocation();

             getUserLocation();

            MyMapFragment.lastKnownLocation.getLatitude();
            idRud++;
            MyMapFragment.getUserLocation();

            Log.v("run()", "Получаем локацию - " +idRud);

        }
}
