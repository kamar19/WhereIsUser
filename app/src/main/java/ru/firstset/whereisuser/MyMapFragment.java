package ru.firstset.whereisuser;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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

import static android.content.Context.LOCATION_SERVICE;

public class MyMapFragment extends Fragment implements
        View.OnClickListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static Context context;
    static GoogleMap googleMap;
    private Button button;
    private CameraPosition cameraPosition;
    private MapView mapView;
    private RequestPermissions requestPermissions;
    private static int PERMISSION_REQUEST_CODE = 152;
    private static boolean locationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    public static final LatLng defaultLocation = new LatLng(52.288288288288285, 76.96901459898946);
    static boolean isGPSEnabled = false;
    static boolean isNetworkEnabled = false;
    static boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_START = 10; // 1 meters
    private static final long MIN_TIME_BW_START = 1000 * 60 * 1; // 1 minute
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 10000; // 10 sec
    private static long minDistance = MIN_DISTANCE_CHANGE_FOR_START;
    private static long minTimeUpdates = MIN_TIME_BW_START;
    private static final int DEFAULT_ZOOM = 15;
    private static SharedPreferences sharedPreferences;
    public static final String BUTTON_KEY = "BUTTON_KEY";
    public static final String POINT_KEY = "POINT_KEY";
    public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";
    private static FusedLocationProviderClient fusedLocationClient;
    public static Location lastKnownLocation;

    static LocationRepository locationRepository;
    static UtilSharedPreferences utilSharedPreferences;
    static int currentTrack;
    public static List<LocationUser> listLocationUser;
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
        context = getActivity();
        button = view.findViewById(R.id.buttonSaveTrack);
        button.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        utilSharedPreferences = new UtilSharedPreferences(sharedPreferences);
        setHasOptionsMenu(true);
        locationRepository = new LocationRepository(getActivity());
        currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
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
        getUserLocation();
        if (!utilSharedPreferences.checkButtonSaveTrack()) {
            currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
            listLocationUser = locationRepository.readLocation(currentTrack);
        }
        if (listLocationUser != null) {
            if (listLocationUser.size() > 0) {
                ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
                for (int i = 0; i < listLocationUser.size(); i++) {
                    latLngs.add(new LatLng(listLocationUser.get(i).latitude, listLocationUser.get(i).longitude));
                }

                Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                        .addAll(latLngs));
                polyline.setWidth(10);
                polyline.setColor(Color.parseColor("#FF0000"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listLocationUser.get(listLocationUser.size() - 1).latitude
                        , listLocationUser.get(listLocationUser.size() - 1).longitude), DEFAULT_ZOOM));

            }
            listLocationUser.clear();
        }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (utilSharedPreferences.checkButtonSaveTrack()) {
            minDistance = MIN_DISTANCE_CHANGE_FOR_START;
            minTimeUpdates = MIN_TIME_BW_START;
            button.setText(getText(R.string.button_save_track));
        } else {
            minDistance = MIN_DISTANCE_CHANGE_FOR_UPDATES;
            minTimeUpdates = MIN_TIME_BW_UPDATES;
            button.setText(getText(R.string.button_stop_track));
            Intent intent = new Intent(Objects.requireNonNull(this.getActivity()).getApplicationContext(), ServiceLocations.class);
            checkLocation();
            this.getActivity().getApplicationContext().startService(intent);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
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
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
                return;
            }
            case 200: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    public void getUserLocation() {
        try {
            lastKnownLocation = null;
            LocationManager locationManager;
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                Log.v("getUserLocation", "1");
                canGetLocation = true;
                if (isNetworkEnabled) {
                    if (locationPermissionGranted) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            return;
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                minDistance,
                                minTimeUpdates, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            lastKnownLocation = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    }
                    if (isGPSEnabled) {
                        if (lastKnownLocation == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    minDistance,
                                    minTimeUpdates, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                lastKnownLocation = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                googleMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            }
                        }
                    }
                }
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }

//                Task<Location> locationResult = fusedLocationClient.getLastLocation();
//                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            lastKnownLocation = task.getResult();
//                            if (lastKnownLocation != null) {
//                                Log.v("onComplete","checkLocation()");
//                                checkLocation();
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(lastKnownLocation.getLatitude(),
//                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
////                                fusedLocationClient.removeLocationUpdates(locationCallback);
//                            }
//                        } else {
//                            Log.v("DEFAULTLoc","defaultLocation");
//
//                            googleMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e) {
//            Log.e("Exception: %s", e.getMessage(), e);
//        }
    }


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
                callIntent();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callIntent() {
        Intent intent = new Intent(Objects.requireNonNull(this.getActivity()).getApplicationContext(), ServiceLocations.class);
        if (utilSharedPreferences.checkButtonSaveTrack()) {
            utilSharedPreferences.saveButtonSaveTrackVisible(false); // Запись и видумость у кнопки Stop
            button.setText(getText(R.string.button_stop_track));
            currentTrack = utilSharedPreferences.loadIdTrack();
            int point = 1;
            utilSharedPreferences.saveIdPoint(point);
            minTimeUpdates = MIN_TIME_BW_UPDATES;
            minDistance = MIN_DISTANCE_CHANGE_FOR_UPDATES; // 1 meters
            getUserLocation();
            checkLocation();
            this.getActivity().getApplicationContext().startService(intent);
        } else {
            currentTrack++;
            utilSharedPreferences.saveIdTrack(currentTrack);
            utilSharedPreferences.saveButtonSaveTrackVisible(true); // Запись и видимость у кнопки Start
            button.setText(getText(R.string.button_save_track));
            minTimeUpdates = MIN_TIME_BW_START;
            minDistance = MIN_DISTANCE_CHANGE_FOR_START; // 1 meters
            getUserLocation();
            checkLocation();
            this.getActivity().getApplicationContext().stopService(intent);
        }
    }

    public static void checkLocation() {
        if (!utilSharedPreferences.checkButtonSaveTrack()) {
            currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
            if (lastKnownLocation != null) {
                int point = utilSharedPreferences.loadIdPoint();
                Double TempLatitude = defaultLocation.latitude;
                Double TempLongitude = defaultLocation.longitude;
                if (point > 2) {
                    utilSharedPreferences.loadIdTrack();
                    listLocationUser = locationRepository.readLocation(currentTrack);
                    if (listLocationUser.size() > 0) {
                        LocationUser locationUserTemp = listLocationUser.get(point - 3);
                        TempLatitude = locationUserTemp.latitude;
                        TempLongitude = locationUserTemp.longitude;
                    }
                }
                if ((!UtilsOther.compareLocations(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()
                        , TempLatitude, TempLongitude)) | (point == 1)) {
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

    @Override
    public void onLocationChanged(Location location) {
        checkLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}



