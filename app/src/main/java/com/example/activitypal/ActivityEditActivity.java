package com.example.activitypal;

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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.activitypal.databinding.ActivityEditBinding;
import com.example.activitypal.utils.APICallHandler;
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

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class ActivityEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "ActivityEditActivity";
    ActivityEditBinding binding;

    Calendar calendar;
    private String address;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(ActivityEditActivity.this, R.layout.activity_edit);
        setContentView(binding.getRoot());

        InitMapsAutoCompleteFrag();
        PromptUserForGalleryImage();

        Bundle extras = getIntent().getExtras();

        binding.editActivityName.setText(extras.getString("activityName"));
        binding.editTextDate.setText(extras.getString("activityDate"));
        binding.editStart.setText(extras.getString("activityStart"));
        binding.editEnd.setText(extras.getString("activityEnd"));

        byte[] decodedStringImg = Base64.getDecoder().decode(extras.getString("activityImg"));
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(this)
                .load(decodedByteImg)
                .override(500, 500)
                .circleCrop()
                .into(binding.editImg);

        binding.editTextDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        // for calendar and date picking dialogs
        binding.editStart.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ActivityEditActivity.this,
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
                            binding.editStart.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, 12, 0, false
            );
            // display previously selected time
            timePickerDialog.updateTime(startHour, startMinute);
            timePickerDialog.show();
        });

        binding.editEnd.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ActivityEditActivity.this,
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
                                binding.editStart.setText(f12Hours.format(date));
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

        binding.editActivityBtn.setOnClickListener(view -> {
            // TODO: send this to the db to update the right activity
            String activityName = binding.editActivityName.getText().toString();

            binding.editImg.setDrawingCacheEnabled(true);
            Bitmap bitmap = binding.editImg.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageByteArray = byteArrayOutputStream.toByteArray();
            String encodedImgByteArray = Base64.getEncoder().encodeToString(imageByteArray);

            String activityDate = binding.editTextDate.getText().toString();
            String startTime = binding.editStart.getText().toString();
            String endTime = binding.editEnd.getText().toString();
            // address already set in InitMapsAutoCompleteFrag()
            APICallHandler.HandleActivityEdit(ActivityEditActivity.this, activityName, encodedImgByteArray,
                    activityDate, startTime, endTime, address, extras.getString("activityId"));
        });
    }

    private void InitMapsAutoCompleteFrag() {
        // This code handles user input for the activity's address
        Places.initialize(getApplicationContext(), BuildConfig.apiKey);
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.edit_autocomplete_fragment);

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
                SetPlaceAddress(place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "onError: " + status);
            }
        });
    }

    private void PromptUserForGalleryImage() {
        // This code is for choosing a photo from the user's photo gallery
        ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Glide.with(ActivityEditActivity.this)
                                .load(result.getData().getData())
                                .override(500, 500)
                                .circleCrop()
                                .into(binding.editImg);
                    }
                });

        binding.editAddPhotoBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mGetContent.launch(intent);
        });
    }

    private void SetPlaceAddress(String address) {
        this.address = address;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        binding.editTextDate.setText(currentDateString);
    }
}