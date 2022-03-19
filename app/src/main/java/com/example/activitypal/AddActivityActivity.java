package com.example.activitypal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;


import com.bumptech.glide.Glide;
import com.example.activitypal.databinding.ActivityAddActivityBinding;
import com.example.activitypal.utils.DatePickerFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddActivityActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddActivityActivity";

    ActivityAddActivityBinding binding;

    Calendar calendar;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;

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

        binding.textDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        binding.startTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddActivityActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (TimePickerDialog.OnTimeSetListener) (timePicker, hour, minute) -> {
                        // Initialize hour and minute
                        startHour = hour;
                        startMinute = minute;
                        String time = startHour + ":" + startMinute;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");

                            // set selected time on text view
                            binding.startTime.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, 12, 0, false
            );
            // display previously selected time
            timePickerDialog.updateTime(startHour, startMinute);
            timePickerDialog.show();
        });

        binding.endingTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddActivityActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            // Initialize hour and minute
                            endHour = hour;
                            endMinute = minute;
                            String time = endHour + ":" + endMinute;
                            SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                            try {
                                Date date = f24Hours.parse(time);
                                SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");

                                // set selected time on text view
                                binding.endingTime.setText(f12Hours.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 12, 0, false
            );
            // display previously selected time
            timePickerDialog.updateTime(endHour, endMinute);
            timePickerDialog.show();
        });

        binding.addActivityBtn.setOnClickListener(view -> {
            // format all time data
            SimpleDateFormat activityDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
            // get all data to be sent to the server
            binding.imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = binding.imageView.getDrawingCache();
            String activityName = binding.activityName.getText().toString();
            Date activityDate = new Date();
            try {
                activityDate = activityDateFormatter.parse(binding.textDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startTime = binding.startTime.getText().toString();
            String endTime = binding.endingTime.getText().toString();
            String location = binding.autocompleteFragment.toString();
            Log.d(TAG, "onCreate: Activity will commence at " + startTime);
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        binding.textDate.setText(currentDateString);
    }
}