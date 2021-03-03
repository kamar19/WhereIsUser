package ru.firstset.whereisuser;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PeriodicWorker extends Worker {
    private static Long workCount = 0L;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH);


    public PeriodicWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @Override
    public Result doWork() {
        try {
            workCount++;
            Log.v("Periodic, doWork()", getCurrentDateTimeString()+" :"+workCount);
        } catch (Exception e) {
            return Result.failure();
        }
        return Result.success();
    }

    public static String getCurrentDateTimeString() {
        String dataStr = sdf.format(new Date());
        return dataStr;
    }
}
