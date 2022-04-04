package com.example.activitypal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.activitypal.models.Activity;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.HomeAdapter;
import com.example.activitypal.utils.MyActivitiesListAdapter;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;
import com.example.activitypal.utils.VolleyCallback;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private String city;
    ArrayList<Activity> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(getContext());
        SetData(credPair, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // get the city name
        if (getArguments() != null) {
            city = getArguments().getString("city");
        }
    }

    public void SetData(Pair<String, String> credPair, View view) {
        // TODO: Fetch activities in the user's area: not activities belonging to the user
        APICallHandler.FetchNearbyActivities(getContext(), city, new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<Activity> result) {
                SetData(result, view);
            }

            @Override
            public void onError(ArrayList<Activity> result) {

            }
        });
    }

    // function must be callable through public SetData()
    private void SetData(ArrayList<Activity> result, View view) {
        // clear data afterwards to avoid duplicate entries
        data.clear();
        // add the data from the response and set the recycler view
        data.addAll(result);
        RecyclerView recyclerView = view.findViewById(R.id.home_activities_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HomeAdapter(data));
    }
}