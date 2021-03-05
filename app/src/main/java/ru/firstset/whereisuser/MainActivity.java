package ru.firstset.whereisuser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.firstset.whereisuser.permission.AppPermission;

public class MainActivity extends AppCompatActivity{


    //public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//    GoogleMap googleMap;
    private static final double TARGET_LATITUDE = 17.893366;
    private static final double TARGET_LONGITUDE = 19.511868;
    public static final List<AppPermission> listPermission = null;

//    SupportMapFragment mapFragment;
    MyMapFragment myMapFragment;
    public static Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.buttonSaveTrack);
        button.setText(getText(R.string.button_save_track));


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            myMapFragment = new MyMapFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frameLayout, myMapFragment)
                    .commit();
        }

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClickLocationSettings(View view) {
        myMapFragment. onClick(view);
    }

}