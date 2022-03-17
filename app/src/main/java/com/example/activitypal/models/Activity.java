package com.example.activitypal.models;

import android.media.Image;

public class Activity {
    private String name;
    private String token;

    public Activity(String name) {
        this.name = name;
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
}
