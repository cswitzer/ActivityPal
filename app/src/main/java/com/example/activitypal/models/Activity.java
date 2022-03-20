package com.example.activitypal.models;

import android.graphics.Bitmap;

import com.squareup.moshi.JsonClass;

public class Activity {
    private String name;
    private String base64ImageString;
    private String token;

    public Activity(String name, String base64ImageString) {
        this.name = name;
        this.base64ImageString = base64ImageString;
        this.token = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBase64ImageString() {
        return base64ImageString;
    }

    public void setBase64ImageString(String base64ImageString) {
        this.base64ImageString = base64ImageString;
    }
}
