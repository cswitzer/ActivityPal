package com.example.activitypal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activitypal.R;
import com.example.activitypal.databinding.FragmentHomeBinding;
import com.example.activitypal.databinding.MyActivitiesListAdapterBinding;
import com.example.activitypal.models.Activity;

import java.util.ArrayList;
import java.util.Base64;

public class MyActivitiesListAdapter extends RecyclerView.Adapter<MyActivitiesListAdapter.MyViewHolder> {
    private static final String TAG = "MyActivitiesListAdapter";

    // create string data arrays for photo, activity names, usernames, locations.
    ArrayList<Activity> data;

    public MyActivitiesListAdapter(ArrayList<Activity> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // in MainActivity, inflate the parent recyclerview with the cardviews in my_activities_list_adapter
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MyActivitiesListAdapterBinding binding = MyActivitiesListAdapterBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.activityElName.setText(data.get(position).getName());
        holder.binding.activityElLocation.setText(data.get(position).getAddress());
        holder.binding.myDate.setText(data.get(position).getDate());
        holder.binding.myActivityStart.setText(data.get(position).getStartTime());
        holder.binding.myActivityEnd.setText(data.get(position).getEndTime());
        holder.binding.myActivityId.setText(data.get(position).get_id());

        // convert base64 string to bitmap
        byte[] decodedStringImg = Base64.getDecoder().decode(data.get(position).getBase64ImageString());
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(holder.itemView.getContext())
                .load(decodedByteImg)
                .override(250, 250)
                .circleCrop()
                .into(holder.binding.activityElPhoto);

        holder.binding.editButton.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: " + holder.binding.myActivityId.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        MyActivitiesListAdapterBinding binding;

        public MyViewHolder(@NonNull MyActivitiesListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
