package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.activitypal.databinding.ActivityDetailsBinding;

import java.util.Base64;

public class ActivityDetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        binding = DataBindingUtil.setContentView(ActivityDetailsActivity.this, R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        binding.activityDetailName.setText(extras.getString("activityName"));
        binding.detailLocation.setText(extras.getString("activityLocation"));
        binding.detailDate.setText(extras.getString("activityDate"));
        binding.detailStart.setText(extras.getString("activityStart"));
        binding.detailEnd.setText(extras.getString("activityEnd"));

        byte[] decodedStringImg = Base64.getDecoder().decode(extras.getString("activityImg"));
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(this)
                .load(decodedByteImg)
                .override(500, 500)
                .circleCrop()
                .into(binding.detailImage);
    }
}