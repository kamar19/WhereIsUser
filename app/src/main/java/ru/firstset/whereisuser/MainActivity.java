package ru.firstset.whereisuser;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback  {
    GoogleMap googleMap;
    private static final double TARGET_LATITUDE = 17.893366;
    private static final double TARGET_LONGITUDE = 19.511868;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String geoUriString = "geo:0,10?z=2";
//        Uri geoUri = Uri.parse(geoUriString);
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
//        if (mapIntent.resolveActivity(getPackageManager()) != null) {
//            startActivity(mapIntent);
//        }
        createMapView();
        addMarker();
    }

    private void createMapView(){

        try {
            if( googleMap==null){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapView);
                mapFragment.getMapAsync(this);

                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }

    }



    private void addMarker(){

        double lat = TARGET_LATITUDE;
        double lng = TARGET_LONGITUDE;
        //устанавливаем позицию и масштаб отображения карты
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);

        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title("Mark")
                    .draggable(false)
            );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("Marker"));
        }
}