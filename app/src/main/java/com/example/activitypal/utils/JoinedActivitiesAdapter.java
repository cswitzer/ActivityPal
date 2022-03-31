package com.example.activitypal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
            APICallHandler.HandleActivityLeave(view.getContext(), holder.binding.joinedActivityId.getText().toString());
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
