package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.activitypal.databinding.ActivityLoginBinding;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();
            SharedPrefsHandler.SaveUserCred(LoginActivity.this, email, password);
            APICallHandler.HandleLogin(LoginActivity.this, email, password);
        });

        binding.switchToRegBtn.setOnClickListener(view -> {
            Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(regIntent);
        });

        // don't make user login again if they already have
        Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(LoginActivity.this);
        if (!credPair.t.equals("") && !credPair.u.equals("")) {
            Log.d(TAG, "onCreate: " + credPair.t + " " + credPair.u);
            // TODO: The user is logged in, so direct them to the MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}