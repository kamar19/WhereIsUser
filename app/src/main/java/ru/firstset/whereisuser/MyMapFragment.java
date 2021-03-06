package ru.firstset.whereisuser;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import android.content.Intent;
import android.location.LocationManager;
import android.view.ViewGroup;
import android.widget.Button;

import ru.firstset.whereisuser.data.UtilLocationUser;
import ru.firstset.whereisuser.data.LocationDatabase;
import ru.firstset.whereisuser.data.LocationUser;
import ru.firstset.whereisuser.data.UtilSharedPreferences;
import ru.firstset.whereisuser.permission.RequestPermissions;
import ru.firstset.whereisuser.services.ServiceLocations;
import ru.firstset.whereisuser.util.DataUtils;
import ru.firstset.whereisuser.location.LocationRepository;

public class MyMapFragment extends Fragment implements
        View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    //    private static zzz fusedLocationClient;
    static GoogleMap googleMap;
    MapView mapView;
    //    private static final String UNIQUE_WORK_NAME = "MapPeriodicJob";
    private RequestPermissions requestPermissions;

    private static boolean locationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    public static final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static SharedPreferences sharedPreferences;
    public static final String BUTTON_KEY = "BUTTON_KEY";
    public static final String POINT_KEY = "POINT_KEY";
    public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";

    private LocationManager locationManager;
    private static FusedLocationProviderClient fusedLocationClient;
    //       private GoogleApiClient mGoogleApiClient;
    private Location currentLocation;
    public static Location lastKnownLocation;
    private CameraPosition cameraPosition;
    MarkerOptions marker;

    static LocationRepository locationRepository;
    static UtilSharedPreferences utilSharedPreferences;
    static int currentTrack;
    static int currentPoint;
    //    static CurrentLocationUser currentLocationUser;
    static Float currentLatitude;
    static Float currentLongitude;
    static UtilLocationUser utilLocationUser;
    Button button;


    public static LocationDatabase locationDatabase;

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

        mapView.onResume(); // needed to get the map to display immediately
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


        // latitude and longitude
        double latitude = 17.385044;
        double longitude = 78.486671;
//
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        button = view.findViewById(R.id.buttonSaveTrack);
        button.setOnClickListener(this);
//        button.setText(getText(R.string.button_save_track));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        getMapAsync(this);
        setHasOptionsMenu(true);
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();

//        locationDatabase = Room.databaseBuilder(getActivity(),
//                LocationDatabase.class, DBContract.TABLE_NAME).build();
        locationRepository = new LocationRepository(getActivity());
        utilLocationUser = new UtilLocationUser();
        currentLatitude = utilLocationUser.getCurrentLatitude(sharedPreferences);
        currentLongitude = utilLocationUser.getCurrentLongitude(sharedPreferences);


    }

    @Override
    public void onStart() {
        Log.e("onStart", "0");

        super.onStart();
        mapView.onStart();
//        mGoogleApiClient.connect();
//        Log.e("onStart", "1");

    }

//@Override
//public void onConnected(Bundle bundle) {
//    if (requestPermissions.checkPermission()) {
//        locationPermissionGranted = true;
//        currentLocation = LocationServices
//                .FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//    } else {
//        requestPermissions.requestPermission();
//    }
//}

    @Override
    public void onStop() {
        super.onStop();
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
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
        Log.e("onMapReady", "0");

        this.googleMap = googleMap;
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        this.googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(currentLatitude,
//                        currentLongitude))
//                .title("Marker"));
        setUpMap();
        getUserLocation();


    }

    public void setUpMap() {

        if (googleMap == null) {
            return;
        }

//        initCamera(currentLocation);
//        Log.e("setUpMap", "initCamera");

        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        try {
            if (locationPermissionGranted) {
                Log.e("setMyLocationEnabled", "true");

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                Log.e("setMyLocationEnabled", "false");

                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        marker = new MarkerOptions().position(
                new LatLng(defaultLocation.latitude,
                        defaultLocation.longitude)).title("Hello Maps");
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        googleMap.addMarker(marker);
        if (cameraPosition == null) {
            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(defaultLocation.latitude, defaultLocation.longitude)).zoom(12).build();
        }
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        checkButtonSaveTrack();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
//        locationPermissionGranted = false;
        getLocationPermission();
//        setUpMap();
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
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
//                            Log.d(, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
        Log.v("currentLatitude", String.valueOf(currentLatitude));
        Log.v("currentLongitude", String.valueOf(currentLongitude));
        utilSharedPreferences = new UtilSharedPreferences(sharedPreferences);
        currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());


        if (lastKnownLocation != null)
            if ((currentLatitude != lastKnownLocation.getLatitude()) & (currentLongitude != lastKnownLocation.getLongitude())) {
                int point = utilSharedPreferences.loadIdPoint();
                locationUser = new LocationUser(point, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                        KEY_LOCATION, currentTrack, DataUtils.getCurrentDateTimeString());
                Log.v("getUserLocation", locationUser.toString());
                Log.v("latitude", locationUser.latitude.toString());
                Log.v("longitude", locationUser.longitude.toString());
                Log.v("time", locationUser.time.toString());
                Log.v("title", locationUser.title.toString());
                Log.v("id", String.valueOf(locationUser.id));
                Log.v("track", String.valueOf(locationUser.track));
//            LocationRepository
                locationRepository.saveLocation(locationUser);
                Log.v("run()", "Получаем локацию - " +locationUser.id);
                point++;
                Log.v("point", String.valueOf(point));
                utilSharedPreferences.saveIdPoint(point);


                utilLocationUser.saveCurrentLocation(sharedPreferences, currentLatitude, currentLongitude);
            } else {
                Log.v("user", "Локация не изменилась! Точка не сохранена");

        }


    }


    public void saveButtonSaveTrackVisible(Boolean value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putBoolean(BUTTON_KEY, value);
            editorSharedPreferences.apply();
            Log.v("saveButton", value.toString());
        }
    }


    public Boolean checkButtonSaveTrack() {
        Boolean boolValue = false;
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        }

        boolValue = sharedPreferences.getBoolean(BUTTON_KEY, true);
        Log.v("checkButtonSaveTrack", boolValue.toString());
        if (boolValue) {
            button.setText(getText(R.string.button_save_track));
        } else {
            button.setText(getText(R.string.button_stop_track));
        }

        return boolValue;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        Log.v("onClick", "0");

        switch (v.getId()) {
            case R.id.buttonLocationSettings: {
                Log.v("onClick", "1");

                startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            }
            case R.id.buttonSaveTrack: {
                Log.v("onClick", "2");
                Intent intent = new Intent(Objects.requireNonNull(this.getActivity()).getApplicationContext(), ServiceLocations.class);
                Log.v("onClick", "intent");

                if (checkButtonSaveTrack()) {
                    Log.v("onClick", "true");
                    saveButtonSaveTrackVisible(false); // Запись и видумость у кнопки Stop
                    button.setText(getText(R.string.button_stop_track));

                    currentTrack=utilSharedPreferences.loadIdTrack();
                    Log.v("currentTrack", String.valueOf(currentTrack));

                    getUserLocation();
                    this.getActivity().getApplicationContext().startService(intent);
                    currentTrack++;
                    utilSharedPreferences.saveIdTrack(currentTrack);

                } else {
                    Log.v("onClick", "false");
                    saveButtonSaveTrackVisible(true); // Запись и видимость у кнопки Start

                    button.setText(getText(R.string.button_save_track));
                    this.getActivity().getApplicationContext().stopService(intent);
                    Log.v("stopService", " end");

                }
                break;
            }
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void onClickLocationSettings(View view) {
//        onClick(view);
//    }
}

