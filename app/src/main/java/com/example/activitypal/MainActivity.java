package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.activitypal.databinding.ActivityMainBinding;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        // don't make user login again if they already have
        Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(MainActivity.this);
        if (!credPair.t.equals("") && !credPair.u.equals("")) {

        }

        binding.button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}