package ru.firstset.whereisuser.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.firstset.whereisuser.R;

public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(String.valueOf(R.string.start_service), "onReceive");
        context.startService(new Intent(context, ServiceLocations.class));
    }
}