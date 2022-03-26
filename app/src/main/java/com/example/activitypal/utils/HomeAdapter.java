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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    ArrayList<Activity> data;

    public HomeAdapter(ArrayList<Activity> data) { this.data = data; }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.activityElementName.setText(data.get(position).getName());
        holder.activityElementLocation.setText(data.get(position).getAddress());
        holder.activityStartTime.setText(data.get(position).getStartTime());
        holder.activityEndTime.setText(data.get(position).getEndTime());
        // holder.activityId.setText(data.get(position).get_id());

        // convert base64 string to bitmap
        byte[] decodedStringImg = Base64.getDecoder().decode(data.get(position).getBase64ImageString());
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(holder.itemView.getContext())
                .load(decodedByteImg)
                .override(250, 250)
                .circleCrop()
                .into(holder.activityElementPhoto);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView activityElementName;
        TextView activityElementLocation;
        TextView activityStartTime;
        TextView activityEndTime;
        TextView activityId;
        ImageView activityElementPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activityElementName = itemView.findViewById(R.id.home_activity_name);
            activityElementPhoto = (ImageView) itemView.findViewById(R.id.home_activity_image);
            activityElementLocation = itemView.findViewById(R.id.home_activity_location);
            activityStartTime = itemView.findViewById(R.id.home_start);
            activityEndTime = itemView.findViewById(R.id.home_end);
            // activityId = itemView.findViewById(R.id.home_hidden_id);
        }
    }
}
