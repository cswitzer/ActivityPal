package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.activitypal.databinding.ActivityRegisterBinding;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.SharedPrefsHandler;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.regButton.setOnClickListener(view -> {
            // NEVER TRUST THE USER!!! VALIDATE AND SANITIZE USER INPUT YOU MADMAN!!!
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString();
            APICallHandler.HandleRegistration(RegisterActivity.this, email, password);
        });
    }
}