package com.example.activitypal.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.activitypal.LoginActivity;
import com.example.activitypal.MainActivity;
import com.example.activitypal.models.User;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

public class APICallHandler {
    private static final String TAG = "APICallHandler";
    private static String baseURL = "http://10.0.2.2:3000/";
    private static RequestQueue rq;
    private static Moshi moshi;
    private static String type;

    public static void HandleRegistration(Context context, String email, String password) {
        type = "Register";
        InitVolleyAndMoshi(context);
        JsonAdapter<User> adapter = moshi.adapter(User.class);
        String userJson = adapter.toJson(new User(email, password));
        StringBuilder updatedURL = new StringBuilder(baseURL).append("users/register");
        // success will be made true if this succeeds
        MakeRequest(context, userJson, updatedURL);

    }

    public static void HandleLogin(Context context, String email, String password) {
        type = "Login";
        InitVolleyAndMoshi(context);
        JsonAdapter<User> adapter = moshi.adapter(User.class);
        String userJson = adapter.toJson(new User(email, password));
        StringBuilder updatedURL = new StringBuilder(baseURL).append("users/login");
        MakeRequest(context, userJson, updatedURL);
    }

    public static void HandleLogout(Context context, String email, String password) {
        type = "Logout";
        InitVolleyAndMoshi(context);
        JsonAdapter<User> adapter = moshi.adapter(User.class);
        // set the user and it's token
        User currentUser = new User(email, password);
        currentUser.setToken(SharedPrefsHandler.GetUserToken(context));

        String userJson = adapter.toJson(currentUser);
        StringBuilder updatedURL = new StringBuilder(baseURL).append("users/logout");
        MakeRequest(context, userJson, updatedURL);
    }

    private static void InitVolleyAndMoshi(Context context) {
        rq = Volley.newRequestQueue(context);
        moshi = new Moshi.Builder().build();
    }

    private static void MakeRequest(Context context, String userJson, StringBuilder updatedURL) {
        JsonObjectRequest postRequest = null;
        try {
            postRequest = new JsonObjectRequest(Request.Method.POST, updatedURL.toString(), new JSONObject(userJson),
                    response -> {
                        try {
                            if (response.getString("status").equals("Approved")) {
                                String token = response.getString("token");
                                HandleResponse(context, type, token);
                            } else {
                                Log.d(TAG, "MakeRequest: Failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                Toast.makeText(context, "There was an error", Toast.LENGTH_LONG).show();
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert postRequest != null;
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(postRequest);
    }

    private static void HandleResponse(Context context, String type, String token) {
        switch (type) {
            case "Register":
                context.startActivity(new Intent(context, LoginActivity.class));
                Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show();
                break;
            case "Login":
                SharedPrefsHandler.SaveUserToken(context, token);
                context.startActivity(new Intent(context, MainActivity.class));
                Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show();
                break;
            case "Logout":
                SharedPrefsHandler.ClearUserPref(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                Toast.makeText(context, "Signing out...", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: Add the login functionality
    // make an api request to server
    // check if the email or hashed version of the password exists in the database
    // create json web token on the server to authenticate logging in
    // store the json web token in the user's shared preferences
}
