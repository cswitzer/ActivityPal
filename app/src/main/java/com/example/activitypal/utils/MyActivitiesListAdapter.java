package com.example.activitypal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activitypal.R;
import com.example.activitypal.models.Activity;

import java.util.ArrayList;
import java.util.Base64;

public class MyActivitiesListAdapter extends RecyclerView.Adapter<MyActivitiesListAdapter.MyViewHolder> {
    // create string data arrays for photo, activity names, usernames, locations.
    ArrayList<Activity> data;

    public MyActivitiesListAdapter(ArrayList<Activity> data) {
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
        holder.activityElementName.setText(data.get(position).getName());
        holder.activityElementLocation.setText(data.get(position).getAddress());


        // convert base64 string to bitmap
        byte[] decodedStringImg = Base64.getDecoder().decode(data.get(position).getBase64ImageString());
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(holder.itemView.getContext())
                .load(decodedByteImg)
                .circleCrop()
                .override(150, 150)
                .into(holder.activityElementPhoto);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // Add a photo, name, timeslot, and creator
        TextView activityElementName;
        TextView activityElementLocation;
        ImageView activityElementPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activityElementName = itemView.findViewById(R.id.activity_el_name);
            activityElementPhoto = (ImageView) itemView.findViewById(R.id.activity_el_photo);
            activityElementLocation = itemView.findViewById(R.id.activity_el_location);
        }
    }
}
