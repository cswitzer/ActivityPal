package com.example.activitypal.models;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private String _id;
    private String name;
    private String base64ImageString;
    private String date;
    private String startTime;
    private String endTime;
    private String address;
    private String city;
    private List<String> participants;
    private String token;

    public Activity(String name, String base64ImageString, String date, String startTime, String endTime, String address) {
        this.name = name;
        this.base64ImageString = base64ImageString;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
