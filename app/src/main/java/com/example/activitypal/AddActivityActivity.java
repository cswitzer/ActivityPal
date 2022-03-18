package com.example.activitypal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.example.activitypal.databinding.ActivityAddActivityBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class AddActivityActivity extends AppCompatActivity {

    private static final String TAG = "AddActivityActivity";

    ActivityAddActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // This code handles user input for the activity's address
        Places.initialize(getApplicationContext(), BuildConfig.apiKey);
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.990490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));

        autocompleteSupportFragment.setCountries("US");
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // add place.getAddress() to database (single out city or town to map to user's current locations)
                Log.d(TAG, "onPlaceSelected: " + place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "onError: " + status);
            }
        });

        // This code is for choosing a photo from the user's photo gallery
        ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Glide.with(AddActivityActivity.this)
                                .load(result.getData().getData())
                                .override(500, 500)
                                .circleCrop()
                                .into(binding.imageView);
                    }
                });

        binding.addPhotoBtn.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mGetContent.launch(intent);
        });
    }
}