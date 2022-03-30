package com.example.activitypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.activitypal.databinding.ActivityMainBinding;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Our app opens through the login activity, which contains a link to the register activity.
// MainActivity will contain local area activities and links to user account information.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int LOCATION_REQUEST_CODE = 10001;
    private List<Address> addresses;

    // initialize fragments
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    JoinedActivitiesFragment joinedActivities = new JoinedActivitiesFragment();
    MyActivitiesFragment myActivitiesFragment = new MyActivitiesFragment();

    ActivityMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Geocoder geocoder;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            // use geocoder to convert global coordinates to a city name
            for (Location location : locationResult.getLocations()) {
                Log.d(TAG, "onLocationResult: " + location.toString());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // String city = addresses.get(0).getLocality();
                // Log.d(TAG, "onLocationResult: " + city);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = new ArrayList<>();

        // TODO: use a fragment to do this, since MainActivity will consists of various fragments
        // the fragments are: my activities, create activities, activities in my area, etc.
//        binding.activityBtn.setOnClickListener(view -> {
//            String name = binding.ActivityName.getText().toString();
//            APICallHandler.HandleActivityAdding(MainActivity.this, name);
//        });

        // set to home fragment upon opening the application
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .commit();
                    return true;
                case R.id.joinedactivities:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, joinedActivities)
                            .commit();
                    return true;
                case R.id.myactivities:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, myActivitiesFragment)
                            .commit();
                    return true;
            }
            return false;
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_menu_item) {// credPair.t is email and credPair.u is password
            // Get user credentials from shared preferences for auth purposes
            Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(MainActivity.this);
            String token = SharedPrefsHandler.GetUserToken(MainActivity.this);
            APICallHandler.HandleLogout(MainActivity.this, credPair.t, credPair.u);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        // Determine if the relevant system settings are enabled on the device to carry out the desired location request.
        // Optionally, invoke a dialog that allows the user to enable the necessary location settings with a single tap.
        SettingsClient client = LocationServices.getSettingsClient(this);

        // Checks the client's settings to see if location requests are possible: an async task
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> {
            // settings of device are satisfied and we start location updates
            startLocationUpdates();
        });
        locationSettingsResponseTask.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException apiException = (ResolvableApiException) e;
                try {
                    apiException.startResolutionForResult(MainActivity.this, 1001);
                } catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                checkSettingsAndStartLocationUpdates();
            } else {
                // permission not granted
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        LOCATION_REQUEST_CODE);
            }
        }
    }
}