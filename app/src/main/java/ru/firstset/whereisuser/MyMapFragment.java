package ru.firstset.whereisuser;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import android.content.Intent;
import android.location.LocationManager;

import ru.firstset.whereisuser.data.UtilLocationUser;
import ru.firstset.whereisuser.data.LocationDatabase;
import ru.firstset.whereisuser.data.LocationUser;
import ru.firstset.whereisuser.data.Track;
import ru.firstset.whereisuser.permission.RequestPermissions;
import ru.firstset.whereisuser.services.ServiceLocations;
import ru.firstset.whereisuser.util.DataUtils;
import ru.firstset.whereisuser.location.LocationRepository;

public class MyMapFragment extends SupportMapFragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    //    private static zzz fusedLocationClient;
    static GoogleMap googleMap;
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
    private GoogleApiClient mGoogleApiClient;
    private Location currentLocation;
    public static Location lastKnownLocation;
    private CameraPosition cameraPosition;

    static LocationRepository locationRepository;
    static Track track;
    static int currentTrack;
    static int currentPoint;
    //    static CurrentLocationUser currentLocationUser;
    static Float currentLatitude;
    static Float currentLongitude;
    static UtilLocationUser utilLocationUser;


    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};

    public static LocationDatabase locationDatabase;

    private int curMapTypeIndex = 1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
//        }
        requestPermissions = new RequestPermissions(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getMapAsync(this);
        setHasOptionsMenu(true);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        track = new Track(sharedPreferences);
        Log.v("onViewCreated","1");
        currentTrack = Integer.valueOf(track.loadIdTrack());
        Log.v("onViewCreated","2");

//        locationDatabase = Room.databaseBuilder(getActivity(),
//                LocationDatabase.class, DBContract.TABLE_NAME).build();
        locationRepository = new LocationRepository(getActivity());
        utilLocationUser = new UtilLocationUser();
        currentLatitude = utilLocationUser.getCurrentLatitude(sharedPreferences);
        currentLongitude = utilLocationUser.getCurrentLongitude(sharedPreferences);

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (requestPermissions.checkPermission()) {
            locationPermissionGranted = true;
            currentLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        } else {
            requestPermissions.requestPermission();
        }
    }

    //    private void initListeners() {
//        googleMap.setOnMarkerClickListener(this);
//        googleMap.setOnMapLongClickListener(this);
//        googleMap.setOnInfoWindowClickListener(this);
//        googleMap.setOnMapClickListener(this);
//    }
//
//    @Override
//    public void onMapClick(LatLng latLng) {
//        MarkerOptions options = new MarkerOptions().position(latLng);
//        options.title(getAddressFromLatLng(latLng));
//        options.icon(BitmapDescriptorFactory.defaultMarker());
//        googleMap.addMarker(options);
//    }
//
//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        MarkerOptions options = new MarkerOptions().position(latLng);
//        options.title(getAddressFromLatLng(latLng));
//        options.icon(BitmapDescriptorFactory.fromBitmap(
//                BitmapFactory.decodeResource(getResources(),
//                        R.mipmap.ic_launcher)));
//        googleMap.addMarker(options);
//    }
//
    @Override
    public void onConnectionSuspended(int i) {
    }

//    private String getAddressFromLatLng(LatLng latLng) {
//        Geocoder geocoder = new Geocoder(getActivity());
//        String address = "";
//        try {
//            address = geocoder
//                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
//                    .get(0).getAddressLine(0);
//        } catch (IOException e) {
//        }
//        return address;
//    }

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        marker.showInfoWindow();
//        return true;
//    }

//    private void drawCircle(LatLng location) {
//        CircleOptions options = new CircleOptions();
//        options.center(location);
//        //Radius in meters
//        options.radius(10);
//       /* options.fillColor( getResources()
//                .getColor( R.color.fill_color ) );
//        options.strokeColor( getResources()
//                .getColor( R.color.stroke_color ) );*/
//        options.strokeWidth(10);
//        googleMap.addCircle(options);
//    }
//
//    private void drawPolygon(LatLng startingLocation) {
//        LatLng point2 = new LatLng(startingLocation.latitude + .001,
//                startingLocation.longitude);
//        LatLng point3 = new LatLng(startingLocation.latitude,
//                startingLocation.longitude + .001);
//
//        PolygonOptions options = new PolygonOptions();
//        options.add(startingLocation, point2, point3);
//
//        /*options.fillColor( getResources()
//                .getColor( R.color.fill_color ) );
//        options.strokeColor( getResources()
//                .getColor( R.color.stroke_color ) );*/
//        options.strokeWidth(10);
//
//        googleMap.addPolygon(options);
//    }
//
//    private void drawOverlay(LatLng location, int width, int height) {
//        GroundOverlayOptions options = new GroundOverlayOptions();
//        options.position(location, width, height);
//
//        options.image(BitmapDescriptorFactory
//                .fromBitmap(BitmapFactory
//                        .decodeResource(getResources(),
//                                R.mipmap.ic_launcher)));
//        googleMap.addGroundOverlay(options);
//    }

    @Override
    public void getMapAsync(OnMapReadyCallback callback) {
        super.getMapAsync(callback);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
//
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        this.googleMap = googleMap;
//        this.googleMap.setOnMyLocationButtonClickListener(this);
//        this.googleMap.setOnMyLocationClickListener(this);
        getLocationPermission();
        setUpMap();
        getUserLocation();
    }

    public void setUpMap() {
        Log.e("setUpMap", "0");

        if (googleMap == null) {
            return;
        }

//        initCamera(currentLocation);
        Log.e("setUpMap", "initCamera");

        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        googleMap.setMyLocationEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // [END maps_current_place_up

    }

    @Override
    public void onResume() {
        super.onResume();
        checkButtonSaveTrack();
//        utilLocationUser =
//
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast toast = Toast.makeText(getContext(), "Permission failed", Toast.LENGTH_SHORT);
//            toast.show();
//            return;
//        }
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000 * 10, 10, locationListener);
//        locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
//                locationListener);
//        checkEnabled();
    }


//    @SuppressLint("SetTextI18n")
//    private void checkEnabled() {
//        Log.v("checkEnabled", locationManager.toString());
//        tvEnabledGPS.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER));
//        Log.v("checkEnabled2", locationManager.toString());
//
//        tvEnabledNet.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
//    }


//    public void startPeriodicWork() {
//        workManager = WorkManager.getInstance(getContext());
////        Constraints constraints = (Constraints)new Constraints.Builder()
////                .build();
////        val periodicWorkRequest: PeriodicWorkRequest =
////                PeriodicWorkRequestBuilder<PeriodicWorker>(
////                        PERIODIC_SERVISE_TIME_DIRATION,
////                PERIODIC_SERVISE_TIME_UNIT
////            )
//
//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWorker.class, 15, PERIODIC_SERVISE_TIME_UNIT)
////                .setConstraints(constraints)
//                .build();
//        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
//        Log.v("startPeriodicWork()", PeriodicWorker.getCurrentDateTimeString());
//
//    }

//    public void stopPeriodicWork() {
//        WorkManager workManager = WorkManager.getInstance();
//        workManager.cancelUniqueWork(UNIQUE_WORK_NAME);
//        Log.v("stopPeriodicWork()", "stop");
//    }

//    @Override
//    public boolean onMyLocationButtonClick() {
//        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
//        // Return false so that we don't consume the event and the default behavior still occurs
//        // (the camera animates to the user's current position).
//        return false;
//    }
//
//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
//    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (requestPermissions.checkPermission()) {
            Log.v("EventCalendar", "start");
            locationPermissionGranted = true;
        } else {
            requestPermissions.requestPermission();
        }
    }
    // [END maps_current_place_location_permission]

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        getLocationPermission();
        setUpMap();
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
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
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
        if (lastKnownLocation!=null)
    if ((currentLatitude != lastKnownLocation.getLatitude()) & (currentLongitude != lastKnownLocation.getLongitude()))
        {
            int point = track.loadIdPoint();
            point++;
            track.saveIdTrack(point);
            locationUser = new LocationUser(point, lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude(),
                    KEY_LOCATION, currentTrack, DataUtils.getCurrentDateTimeString());
            Log.v("getUserLocation",locationUser.toString());
            Log.v("latitude",locationUser.latitude.toString());
            Log.v("longitude",locationUser.longitude.toString());
            Log.v("time",locationUser.time.toString());
            Log.v("title",locationUser.title.toString());
            Log.v("id",String.valueOf(locationUser.id));
            Log.v("track",String.valueOf(locationUser.track));
//            LocationRepository
            locationRepository.saveLocation(locationUser);
            Log.v("getUserLocation","4");

            utilLocationUser.saveCurrentLocation(sharedPreferences, currentLatitude, currentLongitude);
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
            MainActivity.button.setText(getText(R.string.button_save_track));
        } else {
            MainActivity.button.setText(getText(R.string.button_stop_track));
        }

        return boolValue;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                    MainActivity.button.setText(getText(R.string.button_stop_track));

                    this.getActivity().getApplicationContext().startService(intent);
                    currentTrack++;

                    track.saveIdTrack(currentTrack);
                    getUserLocation();
                } else {
                    Log.v("onClick", "false");
                    saveButtonSaveTrackVisible(true); // Запись и видимость у кнопки Start

                    MainActivity.button.setText(getText(R.string.button_save_track));
                    this.getActivity().getApplicationContext().stopService(intent);
                    Log.v("stopService", " end");

                }
                break;
            }
        }
    }
}

