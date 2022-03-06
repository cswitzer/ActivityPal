package com.example.activitypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        // TODO: use a fragment to do this, since MainActivity will consists of various fragments
        // the fragments are: my activities, create activities, activities in my area, etc.
        binding.activityBtn.setOnClickListener(view -> {
            String name = binding.ActivityName.getText().toString();
            APICallHandler.HandleActivityAdding(MainActivity.this, name);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_menu_item) {// credPair.t is email and credPair.u is password
            Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(MainActivity.this);
            String token = SharedPrefsHandler.GetUserToken(MainActivity.this);
            APICallHandler.HandleLogout(MainActivity.this, credPair.t, credPair.u);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}