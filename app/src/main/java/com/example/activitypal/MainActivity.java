package com.example.activitypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.activitypal.databinding.ActivityMainBinding;
import com.example.activitypal.utils.ItemViewModel;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ItemViewModel viewModel;
    ActivityMainBinding binding;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        // get a reference to 'fragment_register' with 'getSelectedItem()'
        viewModel.getSelectedItem().observe(this, item -> {
            if (item.equals(1)) {
                SetRegFragView();
            } else if (item.equals(2)) {
                SetLoginFragView();
            }
        });

        // don't make user login again if they already have
        Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(MainActivity.this);
        if (!credPair.t.equals("") && !credPair.u.equals("")) {
            // TODO: log the user in using API call handler (check to see if this data is actually in the database)
            Log.d(TAG, "");
        }

        Fragment navFragment = new NavFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.test, navFragment)
                .commit();

    }

    private void SetRegFragView() {
        Fragment regFragment = new RegisterFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.my_container, regFragment)
                .commit();
    }

    private void SetLoginFragView() {
        Fragment loginFragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.my_container, loginFragment)
                .commit();
    }

}