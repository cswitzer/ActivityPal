package com.example.activitypal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.activitypal.databinding.ActivityAddActivityBinding;

public class AddActivityActivity extends AppCompatActivity {

    private static final String TAG = "AddActivityActivity";

    ActivityAddActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        binding = DataBindingUtil.setContentView(AddActivityActivity.this, R.layout.activity_add_activity);

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    // binding.imageView.setImageURI(result);
                    Glide.with(AddActivityActivity.this)
                            .load(result)
                            .override(500, 500)
                            .circleCrop()
                            .into(binding.imageView);
                });

        binding.addPhotoBtn.setOnClickListener(view -> {
            mGetContent.launch("image/*");
        });
    }
}