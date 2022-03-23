package com.example.activitypal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.MyActivitiesListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MyActivitiesFragment extends Fragment {
    private static final String TAG = "MyActivitiesFragment";
    String[] data = {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my_activities, container, false);

        SetData();
        RecyclerView recyclerView = view.findViewById(R.id.my_activities_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new MyActivitiesListAdapter(data));

        FloatingActionButton addActivityBtn = view.findViewById(R.id.addActivityBtn);

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    // handle the retrieved image here
                });

        addActivityBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddActivityActivity.class);
            startActivity(intent);
        });

        return view;
    }

    public void SetData() {
        // make api call
        data = APICallHandler.MakeRequestGet(getContext());
    }
}