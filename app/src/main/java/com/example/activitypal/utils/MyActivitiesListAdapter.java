package com.example.activitypal.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activitypal.R;

public class MyActivitiesListAdapter extends RecyclerView.Adapter<MyActivitiesListAdapter.MyViewHolder> {
    // create string data arrays for photo, activity names, usernames, locations.
    String[] data;

    public MyActivitiesListAdapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // in MainActivity, inflate the parent recyclerview with the cardviews in my_activities_list_adapter
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_activities_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // TODO: bind necessary data to each view in the recycler view
        holder.activityElementName.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // Add a photo, name, timeslot, and creator
        TextView activityElementName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activityElementName = itemView.findViewById(R.id.activity_el_name);
        }
    }
}
