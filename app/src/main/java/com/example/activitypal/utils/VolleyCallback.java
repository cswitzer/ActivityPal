package com.example.activitypal.utils;

import com.example.activitypal.models.Activity;

import java.util.ArrayList;

public interface VolleyCallback {
    void onSuccess(ArrayList<Activity> result);
    void onError(ArrayList<Activity> result);
}
