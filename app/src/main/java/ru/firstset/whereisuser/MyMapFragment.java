package ru.firstset.whereisuser;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import android.widget.Toast;

import ru.firstset.whereisuser.data.location.LocationUser;
import ru.firstset.whereisuser.services.GPSTracker;
import ru.firstset.whereisuser.util.UtilSharedPreferences;
import ru.firstset.whereisuser.permission.RequestPermissions;
import ru.firstset.whereisuser.services.ServiceLocations;
import ru.firstset.whereisuser.util.UtilsOther;
import ru.firstset.whereisuser.data.location.LocationRepository;

import static android.content.Context.LOCATION_SERVICE;

public class MyMapFragment extends Fragment implements
        View.OnClickListener, LocationListener,
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
    public static final LatLng defaultLocation = new LatLng(52.288288288288285, 76.96901459898946);
    //                                                         52.48828828828829       76.96901459898946
//                                                             52.28828828828828
    static boolean isGPSEnabled = false;
    static boolean isNetworkEnabled = false;
    static boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    private static final int DEFAULT_ZOOM = 15;
    private static SharedPreferences sharedPreferences;
    public static final String BUTTON_KEY = "BUTTON_KEY";
    public static final String POINT_KEY = "POINT_KEY";
    public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
    public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";
    private static Context context;

    private static LocationManager locationManager;
//    private static LocationManager locationManagerNew;

    private static FusedLocationProviderClient fusedLocationClient;

    //    private Location currentLocation;
    public static Location lastKnownLocation;
    //     Location location; // location
    private static Double current_lattitude;
    private static Double current_longitude;

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
        context = getActivity();
        button = view.findViewById(R.id.buttonSaveTrack);
        button.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        utilSharedPreferences = new UtilSharedPreferences(sharedPreferences);
        setHasOptionsMenu(true);
        locationRepository = new LocationRepository(getActivity());
        currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());

//        locationManagerNew = (LocationManager) context.getSystemService(LOCATION_SERVICE);


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
        getUserLocation();

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
                        , listLocationUser.get(listLocationUser.size() - 1).longitude), DEFAULT_ZOOM));
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

//        GPSTracker gps = new GPSTracker(getContext());
//        int status = 0;
//        if (gps.canGetLocation()) {
//            status = GooglePlayServicesUtil
//                    .isGooglePlayServicesAvailable(getActivity());
//
//            if (status == ConnectionResult.SUCCESS) {
//                current_lattitude = gps.getLatitude();
//                current_longitude = gps.getLongitude();
////                _appPrefs.saveSmsBody("" + current_lattitude);
////                _appPrefs.saveSmsBody("" + current_longitude);
//                Log.d("dashlatlongon", "" + current_lattitude + "-"
//                        + current_longitude);
//
//                if (current_lattitude == 0.0 && current_longitude == 0.0) {
//                    current_lattitude = 22.22;
//                    current_longitude = 22.22;
//
//                }
//
//            } else {
//                current_lattitude = 22.22;
//                current_longitude = 22.22;
//            }
//
//        } else {
////            gps.showSettingsAlert();
//        }


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


//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000 * 10, 10, locationListener);
//        locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
//                locationListener);
//        checkEnabled();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        locationManagerNew.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                MIN_TIME_BW_UPDATES,
//                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
//        locationManagerNew.removeUpdates(this);
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

    public void getUserLocation() {
        try {
//            LocationUser locationUser;



            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    2000, 1, this);
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (locationPermissionGranted) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            lastKnownLocation = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (lastKnownLocation != null) {
//                        latitude = lastKnownLocation.getLatitude();
//                        longitude = lastKnownLocation.getLongitude();
//                    }
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (lastKnownLocation == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                lastKnownLocation = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                googleMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        if (locationManager != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
                            }

                        }
                    }
                }
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }


//        try {
//            if (locationPermissionGranted) {
//                GoogleApiClient LocationApiClient  = getLocationApiClient();
//                locationApiClient.connect();
//                fusedLocationClient.requestLocationUpdates()
//                LocationServices.FusedLocationApi.requestLocationUpdates(locationApiClient,locationRequest, this);
//                Task<Location> locationResult = fusedLocationClient.getLastLocation();
//                locationResult.addOnCompleteListener( new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            lastKnownLocation = task.getResult();
//                            if (lastKnownLocation != null) {
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(lastKnownLocation.getLatitude(),
//                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                            }
//                        } else {
//                            googleMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage(), e);
//        }
//
//
        checkLocation();
//
//
//                Task locationResult = fusedLocationClient.getLastLocation();
//                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map camera position to the current location of the device.
//                            lastKnownLocation = task.getResult();
//                            if(lastKnownLocation!=null) {
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(lastKnownLocation.getLatitude(),
//                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                            }
//                        } else if(lastKnownLocation!=null){
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(lastKnownLocation.getLatitude(),
//                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));}
//
//
//                        else {
//                            googleMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//        } catch (SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }


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
//
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
                    checkLocation();
//                    getUserLocation();
                    this.getActivity().getApplicationContext().startService(intent);
                    utilSharedPreferences.saveIdTrack(currentTrack);
                } else {
                    currentTrack++;

                    utilSharedPreferences.saveButtonSaveTrackVisible(true); // Запись и видимость у кнопки Start
                    button.setText(getText(R.string.button_save_track));
                    this.getActivity().getApplicationContext().stopService(intent);
                }
                break;
            }
        }
    }

//    private static LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//
//            checkLocation();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        ;
//
//        @Override
//        public void onProviderDisabled(String provider) {
////            checkEnabled();
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
////            checkEnabled();
//            if (locationPermissionGranted) {
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
////            showLocation(locationManager.getLastKnownLocation(provider));
//            }
//
//
//        }
//
//
//

    ;

//    private void showLocation(Location location) {
//        if (location == null)
//            return;
//        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//            tvLocationGPS.setText(formatLocation(location));
//        } else if (location.getProvider().equals(
//                LocationManager.NETWORK_PROVIDER)) {
//            tvLocationNet.setText(formatLocation(location));
//        }
//    }

//    private String formatLocation(Location location) {
//        if (location == null)
//            return "";
//        return String.format(
//                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
//                location.getLatitude(), location.getLongitude(), new Date(
//                        location.getTime()));
//    }

//    private void checkEnabled() {
//        tvEnabledGPS.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER));
//        tvEnabledNet.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
//    }
//
//        public void onClickLocationSettings(View view) {
//        }
//    };

    public static void checkLocation() {
        Toast.makeText(
                context,
                context.getText(R.string.check_location),
                Toast.LENGTH_SHORT
        ).show();
        if (!utilSharedPreferences.checkButtonSaveTrack()) {
            //Если нажата кнопка
            currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
            if (lastKnownLocation != null) {
                int point = utilSharedPreferences.loadIdPoint();
                Log.v("point", String.valueOf(point));
                Log.v("currentTrack", String.valueOf(currentTrack));

                Double TempLatitude = defaultLocation.latitude;
                Double TempLongitude = defaultLocation.longitude;

                if (point > 2) {
                    utilSharedPreferences.loadIdTrack();
//                currentTrack = Integer.valueOf(utilSharedPreferences.loadIdTrack());
                    listLocationUser = locationRepository.readLocation(currentTrack);
                    if (listLocationUser.size() > 0) {
                        Log.v(".size()", String.valueOf(listLocationUser.size()));
//                        if (listLocationUser.size()-2>0 ) {
                        Log.v("get(0)", String.valueOf(listLocationUser.get(0).latitude));
//                        Log.v("get(1)", String.valueOf(listLocationUser.get(1).latitude));

                        LocationUser locationUserTemp = listLocationUser.get(point - 3);
                        TempLatitude = locationUserTemp.latitude;
                        TempLongitude = locationUserTemp.longitude;
//                        }
                    }
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

    ;

    @Override
    public void onLocationChanged(Location location) {
        checkLocation();
//        showLocation(location);
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


    static public void checkLocation2() {
        GPSTracker gps = new GPSTracker(context);
        int status = 0;
        Log.v("checkLocation2()", "0");

        if (gps.canGetLocation()) {
            Log.v("checkLocation2()", "1");

            status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(context);

            if (status == ConnectionResult.SUCCESS) {
                Log.v("checkLocation2()", "2");

                lastKnownLocation = gps.getLocation();

                Log.v("checkLocation2()", String.valueOf(lastKnownLocation));
                if (lastKnownLocation != null) {
                    Log.v("checkLocation2()", String.valueOf(lastKnownLocation.getLatitude()));
                    Log.v("checkLocation2()", String.valueOf(lastKnownLocation.getLongitude()));

//                current_lattitude = gps.getLatitude();
//                current_longitude = gps.getLongitude();
//                _appPrefs.saveSmsBody("" + current_lattitude);
//                _appPrefs.saveSmsBody("" + current_longitude);
//                Log.d("dashlatlongon", "" + current_lattitude + "-"
//                        + current_longitude);
//
//                if (current_lattitude == 0.0 && current_longitude == 0.0) {
//                    current_lattitude = 22.22;
//                    current_longitude = 22.22;
//
//                }
//
//            } else {
//                current_lattitude = 22.22;
//                current_longitude = 22.22;
//            }

//        } else {
//            gps.showSettingsAlert();
//        }
//                    checkLocation();
                }
            }
        }


    }



}



