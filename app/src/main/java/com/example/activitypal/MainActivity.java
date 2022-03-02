package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.activitypal.databinding.ActivityMainBinding;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.ItemViewModel;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;

// Our app opens through the login activity, which contains a link to the register activity.
// MainActivity will contain local area activities and links to user account information.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ItemViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        binding.test.setOnClickListener(view -> {
            // credPair.t is email and credPair.u is password
            Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(MainActivity.this);
            String token = SharedPrefsHandler.GetUserToken(MainActivity.this);
            APICallHandler.HandleLogout(MainActivity.this, credPair.t, credPair.u);
        });

        // don't make user login again if they already have
        Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(MainActivity.this);
        if (!credPair.t.equals("") && !credPair.u.equals("")) {
            // TODO: log the user in using API call handler (check to see if this data is actually in the database)
            Log.d(TAG, "");
        }
    }

}