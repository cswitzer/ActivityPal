package com.example.activitypal.models;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String name;
    private String base64ImageString;
    private List<String> participants;
    private String token;

    public Activity(String name, String base64ImageString) {
        this.name = name;
        this.base64ImageString = base64ImageString;
        participants = new ArrayList<>();
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

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
