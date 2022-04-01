package com.example.activitypal.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.activitypal.ActivityDetailsActivity;
import com.example.activitypal.AddActivityActivity;
import com.example.activitypal.LoginActivity;
import com.example.activitypal.databinding.JoinedActivitiesAdapterBinding;
import com.example.activitypal.models.Activity;

import java.util.ArrayList;
import java.util.Base64;

public class JoinedActivitiesAdapter extends RecyclerView.Adapter<JoinedActivitiesAdapter.MyViewHolder> {
    private static final String TAG = "JoinedActivitiesAdapter";

    ArrayList<Activity> data;

    public JoinedActivitiesAdapter(ArrayList<Activity> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        JoinedActivitiesAdapterBinding binding = JoinedActivitiesAdapterBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.joinedActivityName.setText(data.get(position).getName());
        holder.binding.joinedActivityLocation.setText(data.get(position).getAddress());
        holder.binding.date.setText(data.get(position).getDate());
        holder.binding.joinedActivityStart.setText(data.get(position).getStartTime());
        holder.binding.joinedActivityEnd.setText(data.get(position).getEndTime());
        holder.binding.joinedActivityId.setText(data.get(position).get_id());

        byte[] decodedStringImg = Base64.getDecoder().decode(data.get(position).getBase64ImageString());
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(holder.itemView.getContext())
                .load(decodedByteImg)
                .override(250, 250)
                .circleCrop()
                .into(holder.binding.joinedActivityImage);

        holder.binding.leaveButton.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: " + view.getParent().getParent());
            View cardView = (View)view.getParent().getParent();
            cardView.setVisibility(View.GONE);
            APICallHandler.HandleActivityLeave(view.getContext(), holder.binding.joinedActivityId.getText().toString());
        });

        View cardView = (View)holder.binding.leaveButton.getParent().getParent();
        cardView.setOnClickListener(view -> {
            Intent detailsIntent = new Intent(view.getContext(), ActivityDetailsActivity.class);
            detailsIntent.putExtra("activityName", data.get(position).getName());
            detailsIntent.putExtra("activityLocation", data.get(position).getAddress());
            detailsIntent.putExtra("activityDate", data.get(position).getDate());
            detailsIntent.putExtra("activityStart", data.get(position).getStartTime());
            detailsIntent.putExtra("activityEnd", data.get(position).getEndTime());
            detailsIntent.putExtra("activityImg", data.get(position).getBase64ImageString()); // read this in as a Bitmap
            detailsIntent.putExtra("activityId", data.get(position).get_id());
            view.getContext().startActivity(detailsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        JoinedActivitiesAdapterBinding binding;

        public MyViewHolder(@NonNull JoinedActivitiesAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
