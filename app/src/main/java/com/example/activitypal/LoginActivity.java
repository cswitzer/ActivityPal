package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.activitypal.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}