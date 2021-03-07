package ru.firstset.whereisuser;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;

import ru.firstset.whereisuser.data.location.LocationUser;
import ru.firstset.whereisuser.util.UtilSharedPreferences;
import ru.firstset.whereisuser.permission.RequestPermissions;
import ru.firstset.whereisuser.services.ServiceLocations;
import ru.firstset.whereisuser.util.UtilsOther;
import ru.firstset.whereisuser.data.location.LocationRepository;

public class MyMapFragment extends Fragment implements
        View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    static GoogleMap googleMap;
    MapView mapView;
    private RequestPermissions requestPermissions;
    private static boolean locationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    public static final LatLng defaultLocation = new LatLng(52.288288288288275, 76.96901459898956);
//                                                             52.48828828828829       76.96901459898946

    private static final int DEFAULT_ZOOM = 15;
    private static SharedPreferences sharedPreferences;
    public static final String BUTTON_KEY = "BUTTON_KEY";
    public static final String POINT_KEY = "POINT_KEY";
    public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";

    //    private LocationManager locationManager;
    private static FusedLocationProviderClient fusedLocationClient;
    //    private Location currentLocation;
    public static Location lastKnownLocation;
    private CameraPosition cameraPosition;
//    MarkerOptions marker;
//    static LocationListenerMap locationListenerMap;

    static LocationRepository locationRepository;
    static UtilSharedPreferences utilSharedPreferences;
    static int currentTrack;
    //    static int currentPoint;
    //    static CurrentLocationUser currentLocationUser;
//    public static Float currentLatitude;
//    public static Float currentLongitude;
    Button button;
    public static List<LocationUser> listLocationUser;

//    public static LocationDatabase locationDatabase;

    private int curMapTypeIndex = 1;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        requestPermissions = new RequestPermissions(getActivity());
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        button = view.findViewById(R.id.buttonSaveTrack);
        button.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        utilSharedPreferences = new UtilSharedPreferences(sharedPreferences);
        setHasOptionsMenu(true);
        locationRepository = new LocationRepository(getActivity());
        currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
//        currentLatitude = utilSharedPreferences.getCurrentLatitude(sharedPreferences);
//        currentLongitude = utilSharedPreferences.getCurrentLongitude(sharedPreferences);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();
//        getUserLocation();

        if (!utilSharedPreferences.checkButtonSaveTrack()) {
            //Если нажата кнопка, рисуем текущий трек
//            Log.v("currentTrack", String.valueOf(currentTrack));

//            if (lastKnownLocation != null) {
            Log.v("currentTrack", String.valueOf(currentTrack));
            currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
            listLocationUser = locationRepository.readLocation(currentTrack);

            //          }
        }


//        Log.v("listLocationUser", String.valueOf(listLocationUser.size()));

        if (listLocationUser != null) {
            if (listLocationUser.size() > 0) {

//                    Polyline polyline = null;
//                List<LatLng> latLngs = new ArrayList<>();
                ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
                for (int i = 0; i < listLocationUser.size(); i++) {
                    latLngs.add(new LatLng(listLocationUser.get(i).latitude, listLocationUser.get(i).longitude));
                    Log.v("for (int", String.valueOf(listLocationUser.get(i).latitude));
                    Log.v("for (int", String.valueOf(listLocationUser.get(i).longitude));
                }

                for (int j = 0; j < latLngs.size(); j++) {
                    Log.v("latLngs", String.valueOf(latLngs.get(j).latitude));
                }

                Log.v("latLngs.size ", String.valueOf(latLngs.size()));
                Log.v("listLocationUser", String.valueOf(listLocationUser.size()));

                Polyline polyline = googleMap.addPolyline(new PolylineOptions()
//                        .clickable(true)
                        .addAll(latLngs));
                polyline.setWidth(10);
                polyline.setColor(Color.parseColor("#FF0000"));

//                float tempLatitude = UtilsOther.getFloatRoun3(listLocationUser.get(listLocationUser.size() - 1).latitude);
//                float tempLongitude = UtilsOther.getFloatRoun3(listLocationUser.get(listLocationUser.size() - 1).longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listLocationUser.get(listLocationUser.size() - 1).latitude
                        , listLocationUser.get(listLocationUser.size() - 1).longitude), 14));
//                Log.v("latitude", String.valueOf(tempLatitude));
//                Log.v("longitude", String.valueOf(tempLongitude));

            }
            listLocationUser.clear();

//            listLocationUser.get(listLocationUser.size() - 1).longitude;


        }

//
//        Polyline path = googleMap.addPolyline(new PolylineOptions()
//                .add(
//                        new LatLng(38.893596444352134, -77.0381498336792),
//                        new LatLng(38.89337933372204, -77.03792452812195),
//                        new LatLng(38.89316222242831, -77.03761339187622),
//                        new LatLng(38.893028615148424, -77.03731298446655),
//                        new LatLng(38.892920059048464, -77.03691601753235),
//                        new LatLng(38.892903358095296, -77.03637957572937),
//                        new LatLng(38.89301191422077, -77.03592896461487),
//                        new LatLng(38.89316222242831, -77.03549981117249),
//                        new LatLng(38.89340438498248, -77.03514575958252),
//                        new LatLng(38.893596444352134, -77.0349633693695)
//                )
//        );
//
//        // Style the polyline
//        path.setWidth(10);
//        path.setColor(Color.parseColor("#FF0000"));
//
//        // Position the map's camera
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.89399, -77.03659), 16));


    }

    public void setUpMap() {
        if (googleMap == null) {
            return;
        }
        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (utilSharedPreferences.checkButtonSaveTrack()) {
            utilSharedPreferences.saveButtonSaveTrackVisible(true); // Запись и видумость у кнопки Stop
            button.setText(getText(R.string.button_save_track));
        } else {
            utilSharedPreferences.saveButtonSaveTrackVisible(false); // Запись и видумость у кнопки Stop
            button.setText(getText(R.string.button_stop_track));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void getLocationPermission() {
        if (requestPermissions.checkPermission()) {
            locationPermissionGranted = true;
        } else {
            requestPermissions.requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getLocationPermission();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    public static void getUserLocation() {
        LocationUser locationUser;
        try {
            if (locationPermissionGranted) {
                Task locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()&&task.getResult() != null) {
                            lastKnownLocation = (Location)task.getResult();
                            Log.v("onComplete", String.valueOf(lastKnownLocation.getLatitude()));
                            Log.v("onComplete", String.valueOf(lastKnownLocation.getLongitude()));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }

        if (!utilSharedPreferences.checkButtonSaveTrack()) {
            //Если нажата кнопка
            currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
            if (lastKnownLocation != null) {
                int point = utilSharedPreferences.loadIdPoint();
                Log.v("point", String.valueOf(point));

                utilSharedPreferences.loadIdTrack();
//                currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
                listLocationUser = locationRepository.readLocation(currentTrack);
                Double TempLatitude = defaultLocation.latitude;
                Double TempLongitude = defaultLocation.longitude;

                if (point > 1) {
                    Log.v("listLocationUser", String.valueOf(listLocationUser));

                    LocationUser locationUserTemp = listLocationUser.get(point - 2);
                    TempLatitude = locationUserTemp.latitude;
                    TempLongitude = locationUserTemp.longitude;
                }


                Log.v("getLatitude()", String.valueOf(lastKnownLocation.getLatitude()));
                Log.v("getLongitude()", String.valueOf(lastKnownLocation.getLongitude()));
                Log.v("currentLatitude", String.valueOf(TempLatitude));
                Log.v("currentLongitude", String.valueOf(TempLongitude));

                if ((!UtilsOther.compareLocations(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()
                        , TempLatitude, TempLongitude)) | (point == 1)) {
//                    currentLatitude = (float) lastKnownLocation.getLatitude();
//                    currentLongitude = (float) lastKnownLocation.getLongitude();

//                    locationUser = new LocationUser(point, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
//                            KEY_LOCATION, currentTrack, UtilsOther.getCurrentDateTimeString());
//                    locationUser.latitude = locationUser.latitude;
                    locationRepository.saveLocation(new LocationUser(point, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                            KEY_LOCATION, currentTrack, UtilsOther.getCurrentDateTimeString()));
                    point++;
                    utilSharedPreferences.saveIdPoint(point);
                    utilSharedPreferences.saveCurrentLocation(sharedPreferences, (float) lastKnownLocation.getLatitude(), (float) lastKnownLocation.getLongitude());
                    Log.v("getUserLocation", "Save " + String.valueOf(lastKnownLocation.getLatitude()) + ", " + String.valueOf(lastKnownLocation.getLongitude()));

                } else {
                    Log.v("getUserLocation", "Локация не изменилась! Точка не сохранена");
                }
            }
        }
    }

//    public static void drawPoliline(LatLng latLng) {
//
//        PolylineOptions polylineOptions = new PolylineOptions();
////        Polyline polylineTemp = new  Polyline();
//        MyMapFragment.googleMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(latLng)
//        );
//        Log.v("drawPoliline", String.valueOf(latLng.latitude));
//        Log.v("drawPoliline", String.valueOf(latLng.longitude));
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLocationSettings: {
                startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            }
            case R.id.buttonSaveTrack: {
                Intent intent = new Intent(Objects.requireNonNull(this.getActivity()).getApplicationContext(), ServiceLocations.class);
                if (utilSharedPreferences.checkButtonSaveTrack()) {
                    utilSharedPreferences.saveButtonSaveTrackVisible(false); // Запись и видумость у кнопки Stop
                    button.setText(getText(R.string.button_stop_track));
                    currentTrack = utilSharedPreferences.loadIdTrack();
                    int point = 1;
                    utilSharedPreferences.saveIdPoint(point);
                    getUserLocation();
                    this.getActivity().getApplicationContext().startService(intent);
                    currentTrack++;
                    utilSharedPreferences.saveIdTrack(currentTrack);
                } else {
                    utilSharedPreferences.saveButtonSaveTrackVisible(true); // Запись и видимость у кнопки Start
                    button.setText(getText(R.string.button_save_track));
                    this.getActivity().getApplicationContext().stopService(intent);
                }
                break;
            }
        }
    }

}

