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
import ru.firstset.whereisuser.util.UtilLocationUser;
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
    public static final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
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
    static Float currentLatitude;
    static Float currentLongitude;
    static UtilLocationUser utilLocationUser;
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
        utilLocationUser = new UtilLocationUser();
        currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
        currentLatitude = utilLocationUser.getCurrentLatitude(sharedPreferences);
        currentLongitude = utilLocationUser.getCurrentLongitude(sharedPreferences);
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
        if (listLocationUser != null) {
            if (listLocationUser.size() > 0) {
                Polyline polyline = null;
                List<LatLng> latLngs = new ArrayList<>();
                for (int i = 0; i < listLocationUser.size() - 1; i++) {
                    latLngs.add(new LatLng(listLocationUser.get(i).latitude, listLocationUser.get(i).longitude));
//                    Log.v("onItemClick", String.valueOf(listLocationUser.get(i).latitude + i / 10000));
//                    Log.v("onItemClick", String.valueOf(listLocationUser.get(i).longitude + i / 10000));
                }
                if (latLngs != null) {
                    polyline = googleMap.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .addAll(latLngs));
                }
                polyline.setWidth(10);
                polyline.setColor(Color.parseColor("#FF0000"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        listLocationUser.get(listLocationUser.size() - 1).latitude, listLocationUser.get(listLocationUser.size() - 1).longitude), 14));
                listLocationUser.clear();
            }
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
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
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
        if (!utilSharedPreferences.checkButtonSaveTrack()) {
//            if (lastKnownLocation != null) {
//                Log.v("getLatitude()", String.valueOf(lastKnownLocation.getLatitude()));
//                Log.v("getLongitude()", String.valueOf(lastKnownLocation.getLongitude()));
//            Log.v("currentLatitude", String.valueOf(currentLatitude));
//            Log.v("currentLongitude", String.valueOf(currentLongitude));
            currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
            if (lastKnownLocation != null) {
                if (!UtilsOther.compareLocations((float) lastKnownLocation.getLatitude(), (float) lastKnownLocation.getLongitude()
                        , currentLatitude, currentLongitude)) {
                    int point = utilSharedPreferences.loadIdPoint();
                    locationUser = new LocationUser(point, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                            KEY_LOCATION, currentTrack, UtilsOther.getCurrentDateTimeString());
                    locationUser.latitude = locationUser.latitude + 0.2;
                    locationRepository.saveLocation(locationUser);
                    point++;
                    utilSharedPreferences.saveIdPoint(point);
                    utilLocationUser.saveCurrentLocation(sharedPreferences, (float) lastKnownLocation.getLatitude(), (float) lastKnownLocation.getLongitude());
                } else {
                    Log.v("user", "Локация не изменилась! Точка не сохранена");
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

