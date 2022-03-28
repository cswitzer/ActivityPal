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
import com.example.activitypal.HomeFragment;
import com.example.activitypal.R;
import com.example.activitypal.databinding.FragmentHomeBinding;
import com.example.activitypal.databinding.HomeListAdapterBinding;
import com.example.activitypal.models.Activity;

import java.util.ArrayList;
import java.util.Base64;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private static final String TAG = "HomeAdapter";
    ArrayList<Activity> data;

    public HomeAdapter(ArrayList<Activity> data) { this.data = data; }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HomeListAdapterBinding binding = HomeListAdapterBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.homeActivityName.setText(data.get(position).getName());
        holder.binding.homeActivityLocation.setText(data.get(position).getAddress());
        holder.binding.homeStart.setText(data.get(position).getStartTime());
        holder.binding.homeEnd.setText(data.get(position).getEndTime());
        holder.binding.homeHiddenId.setText(data.get(position).get_id());

        // convert base64 string to bitmap
        byte[] decodedStringImg = Base64.getDecoder().decode(data.get(position).getBase64ImageString());
        Bitmap decodedByteImg = BitmapFactory.decodeByteArray(decodedStringImg, 0, decodedStringImg.length);
        Glide.with(holder.itemView.getContext())
                .load(decodedByteImg)
                .override(250, 250)
                .circleCrop()
                .into(holder.binding.homeActivityImage);

        holder.binding.joinButton.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: " + holder.binding.homeHiddenId.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        HomeListAdapterBinding binding;

        public MyViewHolder(@NonNull HomeListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
