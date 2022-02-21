package com.example.activitypal.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.activitypal.models.User;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

public class APICallHandler {
    private static final String TAG = "APICallHandler";
    private static String baseURL = "http://10.0.2.2:8000/";
    private static RequestQueue rq;
    private static Moshi moshi;

    public static void HandleRegistration(Context context, String email, String password) {
        // TODO: change baseURL to append register instead of user
        InitVolleyAndMoshi(context);
        JsonAdapter<User> adapter = moshi.adapter(User.class);
        String userJson = adapter.toJson(new User(email, password));
        StringBuilder updatedURL = new StringBuilder(baseURL).append("users/register");

        MakeRegisterRequest(context, userJson, updatedURL);
    }

    private static void InitVolleyAndMoshi(Context context) {
        rq = Volley.newRequestQueue(context);
        moshi = new Moshi.Builder().build();
    }

    private static void MakeRegisterRequest(Context context, String userJson, StringBuilder updatedURL) {
        JsonObjectRequest postRequest = null;
        try {
            postRequest = new JsonObjectRequest(Request.Method.POST, updatedURL.toString(), new JSONObject(userJson),
                    response -> {
                        try {
                            if (response.getString("status").equals("Approved")) {
                                // Intent intent = new Intent(context, LoginActivity.class);
                                // context.startActivity(intent);

                            } else {
                                Log.d(TAG, "MakeRegisterRequest: It did not work");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                Log.d("RegisterActivity", error.getMessage());
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(postRequest);
    }

    // TODO: Add the login functionality
    // make an api request to server
    // check if the email or hashed version of the password exists in the database
    // create json web token on the server to authenticate logging in
    // store the json web token in the user's shared preferences
}
