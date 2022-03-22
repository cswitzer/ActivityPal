package com.example.activitypal.models;

public class User {
    private String username;
    private String email;
    private String password;
    private String token;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        token = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
