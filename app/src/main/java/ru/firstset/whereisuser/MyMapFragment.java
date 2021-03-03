package ru.firstset.whereisuser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;

import ru.firstset.whereisuser.permission.RequestPermissions;

public class MyMapFragment extends SupportMapFragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "TAG";
    GoogleMap googleMap;
    private static final double TARGET_LATITUDE = 17.893366;
    private static final double TARGET_LONGITUDE = 19.511868;
    private static final String UNIQUE_WORK_NAME = "MoviePeriodicJob";
    private static final Long PERIODIC_SERVISE_TIME_DIRATION = 15L;
    private static final TimeUnit PERIODIC_SERVISE_TIME_UNIT = TimeUnit.MINUTES;

    private RequestPermissions requestPermissions;

    //    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;


    private WorkManager workManager;
    TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvStatusNet;
    TextView tvLocationNet;

    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private Location currentLocation;
    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};

    private int curMapTypeIndex = 1;
    private PeriodicWorker periodicWorker;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        requestPermissions = new RequestPermissions(getActivity());

        tvEnabledGPS = (TextView) view.findViewById(R.id.tvEnabledGPS);
        tvStatusGPS = (TextView) view.findViewById(R.id.tvStatusGPS);
        tvLocationGPS = (TextView) view.findViewById(R.id.tvLocationGPS);
        tvEnabledNet = (TextView) view.findViewById(R.id.tvEnabledNet);
        tvStatusNet = (TextView) view.findViewById(R.id.tvStatusNet);
        tvLocationNet = (TextView) view.findViewById(R.id.tvLocationNet);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

//        stopPeriodicWork();
        startPeriodicWork();

        getMapAsync(this);

        setHasOptionsMenu(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

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
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            Log.v("onSuccess","Logic to handle location object");
//                        }
//                    }
//                });
//        initListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.e("onStart()", "mGoogleApiClient.connect()");

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.e("onStop()", "mGoogleApiClient.disconnect()");

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        Log.e("onConnected", "end");

    }


//    private void initCamera(Location location) {
//
//        CameraPosition position = CameraPosition.builder()
//                .target(new LatLng(location.getLatitude(),
//                        location.getLongitude()))
//                .zoom(16f)
//                .bearing(0.0f)
//                .tilt(0.0f)
//                .build();
//
////        CameraPosition newCamPos = new CameraPosition(new LatLng(13.0810,80.2740),
////                15.5f,
////                map.getCameraPosition().tilt, //use old tilt
////                map.getCameraPosition().bearing); //use old bearing
////        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);
////
////        googleMap = new GoogleMap();
//        Log.v("initCamera", googleMap.toString());
//
//        googleMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(position), null);
//
//    }

    private void initListeners() {
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));

        options.icon(BitmapDescriptorFactory.defaultMarker());
        googleMap.addMarker(options);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));

        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher)));

        googleMap.addMarker(options);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }


    private void drawCircle(LatLng location) {
        CircleOptions options = new CircleOptions();
        options.center(location);
        //Radius in meters
        options.radius(10);
       /* options.fillColor( getResources()
                .getColor( R.color.fill_color ) );
        options.strokeColor( getResources()
                .getColor( R.color.stroke_color ) );*/
        options.strokeWidth(10);
        googleMap.addCircle(options);
    }

    private void drawPolygon(LatLng startingLocation) {
        LatLng point2 = new LatLng(startingLocation.latitude + .001,
                startingLocation.longitude);
        LatLng point3 = new LatLng(startingLocation.latitude,
                startingLocation.longitude + .001);

        PolygonOptions options = new PolygonOptions();
        options.add(startingLocation, point2, point3);

        /*options.fillColor( getResources()
                .getColor( R.color.fill_color ) );
        options.strokeColor( getResources()
                .getColor( R.color.stroke_color ) );*/
        options.strokeWidth(10);

        googleMap.addPolygon(options);
    }

    private void drawOverlay(LatLng location, int width, int height) {
        GroundOverlayOptions options = new GroundOverlayOptions();
        options.position(location, width, height);

        options.image(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.ic_launcher)));
        googleMap.addGroundOverlay(options);
    }

    @Override
    public void getMapAsync(OnMapReadyCallback callback) {
        super.getMapAsync(callback);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("onMapReady", "0");

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        this.googleMap = googleMap;
        Log.e("onMapReady", "googleMap=googleMap");

        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setOnMyLocationClickListener(this);
//        enableMyLocation();
        getLocationPermission();
        setUpMap();

        getDeviceLocation();
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


    @Override
    public void onPause() {
        super.onPause();
//        locationManager.removeUpdates(locationListener);
    }
//
//    private LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            showLocation(location);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            checkEnabled();
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            checkEnabled();
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            showLocation(locationManager.getLastKnownLocation(provider));
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (status != 0) {
//                if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                    tvStatusGPS.setText("Status: " + String.valueOf(status));
//                } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
//                    tvStatusNet.setText("Status: " + String.valueOf(status));
//                }
//            }
//        }
//    };

//    private void showLocation(Location location) {
//        if (location == null)
//            return;
//        else if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//            tvLocationGPS.setText(formatLocation(location));
//        } else if (location.getProvider().equals(
//                LocationManager.NETWORK_PROVIDER)) {
//            tvLocationNet.setText(formatLocation(location));
//        }
//    }
//
//    @SuppressLint("DefaultLocale")
//    private String formatLocation(Location location) {
//        if (location == null)
//            return "";
//        else
//            return String.format(
//                    "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
//                    location.getLatitude(), location.getLongitude(), new Date(
//                            location.getTime()));
//    }

    @SuppressLint("SetTextI18n")
    private void checkEnabled() {
        Log.v("checkEnabled", locationManager.toString());
        tvEnabledGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        Log.v("checkEnabled2", locationManager.toString());

        tvEnabledNet.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void onClick(View v) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void startPeriodicWork() {
        workManager = WorkManager.getInstance(getContext());
        Constraints constraints = (Constraints)new Constraints.Builder()
                .build();
//        val periodicWorkRequest: PeriodicWorkRequest =
//                PeriodicWorkRequestBuilder<PeriodicWorker>(
//                        PERIODIC_SERVISE_TIME_DIRATION,
//                PERIODIC_SERVISE_TIME_UNIT
//            )

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWorker.class, PERIODIC_SERVISE_TIME_DIRATION, PERIODIC_SERVISE_TIME_UNIT)
                .setConstraints(constraints)
                .build();
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
        Log.v("startPeriodicWork()", PeriodicWorker.getCurrentDateTimeString());

    }

    public void stopPeriodicWork() {
        WorkManager workManager = WorkManager.getInstance();
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME);
        Log.v("stopPeriodicWork()", "stop");
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

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
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
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

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
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
    }
}

