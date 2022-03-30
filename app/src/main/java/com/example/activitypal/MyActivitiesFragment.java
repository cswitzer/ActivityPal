package com.example.activitypal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.activitypal.models.Activity;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.MyActivitiesListAdapter;
import com.example.activitypal.utils.Pair;
import com.example.activitypal.utils.SharedPrefsHandler;
import com.example.activitypal.utils.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyActivitiesFragment extends Fragment {
    private static final String TAG = "MyActivitiesFragment";
    ArrayList<Activity> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_activities, container, false);
        Pair<String, String> credPair = SharedPrefsHandler.GetCredPref(getContext());
        SetData(credPair, view);

        FloatingActionButton addActivityBtn = view.findViewById(R.id.addActivityBtn);

        addActivityBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddActivityActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public void SetData(Pair<String, String> credPair, View view) {
        // make api call
        APICallHandler.FetchMyActivities(getContext(), new VolleyCallback() {
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
        RecyclerView recyclerView = view.findViewById(R.id.my_activities_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyActivitiesListAdapter(data));
    }
}