package ru.firstset.whereisuser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.TimerTask;

import ru.firstset.whereisuser.data.TrackPolyline;
import ru.firstset.whereisuser.location.LocationListenerMap;
import ru.firstset.whereisuser.permission.AppPermission;

public class MainActivity extends AppCompatActivity {


    //public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//    GoogleMap googleMap;
    private static final double TARGET_LATITUDE = 17.893366;
    private static final double TARGET_LONGITUDE = 19.511868;
    private static final String FRAGMENT_MAP = "FRAGMENT_MAP";
    private static final String FRAGMENT_HISTORY = "FRAGMENT_HISTORY";
    public static final List<AppPermission> listPermission = null;
    public static List<TrackPolyline> listTrackPolyline;

    //    SupportMapFragment mapFragment;
    MyMapFragment myMapFragment;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
//            fragmentManager = getSupportFragmentManager();
//            myMapFragment = new MyMapFragment();
//            fragmentManager.beginTransaction()
//                    .add(R.id.frameLayoutContainer, myMapFragment, FRAGMENT_MAP)
//                    .commit();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutContainer, new MyMapFragment(), FRAGMENT_MAP)
                    .commit();
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id. mapView);

        }

//        mapFragment.getMapAsync(this);


//        Button button = findViewById(R.id.buttonSaveTrack);
//        Boolean booleanValue =myMapFragment.checkButtonSaveTrack();
//        if (booleanValue) {
//            button.setText(getText(R.string.button_stop_track)); ;
//        } else
//            button.setText(getText(R.string.button_save_track)); ;
//


    }

    private void initCamera(Location location) {

        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
    }
//
//    private void createMapView(){
//
//        try {
//            if( googleMap==null){
//
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
////                mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                assert mapFragment != null;
////                mapFragment.getMapAsync(this);
//                myMapFragment = new MyMapFragment();
//                fragmentManager.beginTransaction()
//                        .add(R.id.frameLayout, myMapFragment)
//                        .commit();
//
//                if(null == googleMap) {
//                    Toast.makeText(getApplicationContext(),
//                            "Error creating map",Toast.LENGTH_SHORT).show();
//                }
//            }
//        } catch (NullPointerException exception){
//            Log.e("mapApp", exception.toString());
//        }
//
//    }

    //
//
//    private void addMarker(){
//
//        double lat = TARGET_LATITUDE;
//        double lng = TARGET_LONGITUDE;
//        //устанавливаем позицию и масштаб отображения карты
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(lat, lng))
//                .zoom(15)
//                .build();
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        googleMap.animateCamera(cameraUpdate);
//
//        if(null != googleMap){
//            googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(lat, lng))
//                    .title("Mark")
//                    .title("Mark")
//                    .draggable(false)
//            );
//        }
//
//    }
//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMap: {
//                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.mapView);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayoutContainer, new MyMapFragment(), FRAGMENT_MAP)
                        .commit();

//                MyMapFragment mapFragment = new MyMapFragment();
//                fragmentManager.beginTransaction()
//                        .addToBackStack(FRAGMENT_MAP)
//                        .replace(R.id.frameLayoutContainer, mapFragment, FRAGMENT_MAP)
//                        .commit();
                break;
            }
            case R.id.menuHistory: {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.findFragmentByTag(FRAGMENT_HISTORY);
                fragmentManager.beginTransaction()
                        .addToBackStack(FRAGMENT_HISTORY)
                        .replace(R.id.frameLayoutContainer, new FragmentHistory(), FRAGMENT_HISTORY)
                        .commit();


                break;

            }
            case R.id.menuExit:
                this.finish();
                break;
        }
        //headerView.setText(item.getTitle());
        return super.onOptionsItemSelected(item);
    }
//    public void runOnUiThread(new Runnable() {
//        public void run(){
//            mGoogleMap.addPolyline(new PolylineOptions().add(latLng));
//            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng)
//                    .zoom(13).build()));
//        }
//    });


    public void runThread() {

        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            MyMapFragment.lastKnownLocation.getLatitude();
//                            idRud++;
                            MyMapFragment.getUserLocation();
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public class LocationTimerTask extends TimerTask {
//    private static int idRud=0;
//    LocationListenerMap locationListenerMap;


        @Override
        public void run() {
            // Получаем локацию, возможно сравниваем с предыдущей и записываем если новая в БД
            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        MyMapFragment.lastKnownLocation.getLatitude();
//                            idRud++;
                        MyMapFragment.getUserLocation();
                    }
                });
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


//        new Thread() {
//        public void run() {
//            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        MyMapFragment.lastKnownLocation.getLatitude();
////                            idRud++;
//                        MyMapFragment.getUserLocation();
//                    }
//                });
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }.start();
        }

    }
}