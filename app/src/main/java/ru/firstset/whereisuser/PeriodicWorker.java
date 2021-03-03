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
//            saveMoviesToDB();
            workCount++;
            Log.v("Periodic, doWork()", getCurrentDateTimeString()+" :"+workCount);
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }


    public static String getCurrentDateTimeString() {
        String dataStr = sdf.format(new Date());

        return dataStr;

    }
}

//: Context, params: WorkerParameters) :
//        CoroutineWorker(context, params),
//        KoinComponent {
//        val repositoryNet: RepositoryNet by inject()
//        val repositoryDB: RepositoryDB by inject()
//
//        override suspend fun doWork(): Result {
//        return withContext(Dispatchers.IO) {
//        try {
//        saveMoviesToDB()
//        Log.v("Periodic, doWork()", getCurrentDateTimeString())
//        return@withContext Result.success()
//        } catch (e: Exception) {
//        return@withContext Result.failure()
//        }
//        }
//        }
//
//        suspend fun saveMoviesToDB() {
//        val moviesFromNet: MutableList<Movie> = mutableListOf()
//        moviesFromNet.addAll(repositoryNet.loadMoviesFromNET(SeachMovie.MovieNowPlaying.seachMovie))
//        moviesFromNet.addAll(repositoryNet.loadMoviesFromNET(SeachMovie.MoviePopular.seachMovie))
//        moviesFromNet.addAll(repositoryNet.loadMoviesFromNET(SeachMovie.MovieTopRated.seachMovie))
//        moviesFromNet.addAll(repositoryNet.loadMoviesFromNET(SeachMovie.MovieUpComing.seachMovie))
//        moviesFromNet.let {
//        repositoryDB.saveMoviesToDB(moviesFromNet, SeachMovie.MovieNowPlaying)
//        Log.v("saveMoviesToDB", " ${getCurrentDateTimeString()} size: ${moviesFromNet.size}")
//        }
//        }
//        }