//package ru.firstset.whereisuser.location;
//
//import android.app.Activity;
//import android.location.Location;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
//import ru.firstset.whereisuser.permission.RequestPermissions;
//
//public class LocationMap {
// public GoogleMap googleMap;
//    private RequestPermissions requestPermissions;
//
//
//
//    public LocationMap(Activity activity, RequestPermissions requestPermissions ) {
//        this.googleMap = googleMap;
//        requestPermissions = new RequestPermissions(activity);
//        this.requestPermissions = requestPermissions;
//    }
//
//
//    public void setUpMap() {
//        Log.e("setUpMap", "0");
//
//        if (googleMap == null) {
//            return;
//        }
//
////        initCamera(currentLocation);
//        Log.e("setUpMap", "initCamera");
//
//        googleMap.setTrafficEnabled(true);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        googleMap.setIndoorEnabled(true);
//        googleMap.setBuildingsEnabled(true);
//        try {
//            if (locationPermissionGranted) {
//                googleMap.setMyLocationEnabled(true);
//                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//            } else {
//                googleMap.setMyLocationEnabled(false);
//                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                lastKnownLocation = null;
//                getLocationPermission();
//            }
//        } catch (SecurityException e) {
//            Log.e("Exception: %s", e.getMessage());
//        }
//
//
//
//
//    public void getLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (locationPermissionGranted) {
//                Task<Location> locationResult = fusedLocationClient.getLastLocation();
//                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
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
////                            Log.d(, "Current location is null. Using defaults.");
////                            Log.e(TAG, "Exception: %s", task.getException());
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
//    }
//
//
//}
