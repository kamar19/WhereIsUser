package ru.firstset.whereisuser.services;

import android.util.Log;

import java.util.TimerTask;

public class LocationTimerTask extends TimerTask {
    private static int idRud=0;

        @Override
        public void run() {
            // Получаем локацию, возможно сравниваем с предыдущей и записываем если новая в БД
            idRud++;
            Log.v("run()", "Получаем локацию - " +idRud);

        }
}
