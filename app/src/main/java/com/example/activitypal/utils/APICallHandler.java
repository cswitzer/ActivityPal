package com.example.activitypal.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.activitypal.LoginActivity;
import com.example.activitypal.MainActivity;
import com.example.activitypal.models.Activity;
import com.example.activitypal.models.User;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APICallHandler {
    private static final String TAG = "APICallHandler";
    private static String baseURL = "http://10.0.2.2:3000/";
    private static RequestQueue rq;
    private static Moshi moshi;
    private static String type;

    public static void HandleRegistration(Context context, String username, String email, String password) {
        type = "Register";
        InitVolleyAndMoshi(context);
        JsonAdapter<User> adapter = moshi.adapter(User.class);
        User userToRegister = new User(email, password);
        userToRegister.setUsername(username);
        String userJson = adapter.toJson(userToRegister);
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

    public static void HandleActivityAdding(Context context, String name, String base64ImageString, String date,
                                            String startTime, String endTime, String address) {
        type = "CreateActivity";
        InitVolleyAndMoshi(context);
        JsonAdapter<Activity> adapter = moshi.adapter(Activity.class);
        // set token so that users are authenticated before adding an activity
        Activity userActivity = new Activity(name, base64ImageString, date, startTime, endTime, address);
        userActivity.setToken(SharedPrefsHandler.GetUserToken(context));
        String activityJson = adapter.toJson(userActivity);
        StringBuilder updatedURL = new StringBuilder(baseURL).append("activities");
        MakeRequest(context, activityJson, updatedURL);
    }

    private static void InitVolleyAndMoshi(Context context) {
        rq = Volley.newRequestQueue(context);
        moshi = new Moshi.Builder().build();
    }

    private static void HandleResponse(Context context, String type, String token) {
        switch (type) {
            case "Register":
                context.startActivity(new Intent(context, LoginActivity.class));
                Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show();
                break;
            case "Login":
                SharedPrefsHandler.SaveUserToken(context, token);
                Log.d(TAG, "HandleResponse: " + token);
                context.startActivity(new Intent(context, MainActivity.class));
                Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show();
                break;
            case "Logout":
                SharedPrefsHandler.ClearUserPref(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                Toast.makeText(context, "Signing out...", Toast.LENGTH_SHORT).show();
                break;
            case "CreateActivity":
                Log.d(TAG, "HandleResponse: Create Activity");
                break;
            case "DeleteActivity":
                Log.d(TAG, "HandleResponse: Delete Activity");
                break;
            case "ViewActivity":
                Log.d(TAG, "HandleResponse: View Activity");
                break;
            case "FetchMyActivities":
                Log.d(TAG, "HandleResponse: Fetch My Activities");
                break;
        }
    }

    // for storing purposes
    private static void MakeRequest(Context context, String json, StringBuilder updatedURL) {
        JsonObjectRequest postRequest = null;
        try {
            postRequest = new JsonObjectRequest(Request.Method.POST, updatedURL.toString(), new JSONObject(json),
                    response -> {
                        try {
                            if (response.getString("status").equals("Approved")) {
                                String token = response.getString("token");
                                HandleResponse(context, type, token);
                            } else if (response.getString("status").equals("AApproved")) {
                                Toast.makeText(context, "Activity added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                // make sure activities' page only shows if login attempt is successful
                if (type.equals("Login")) {
                    SharedPrefsHandler.SaveUserCred(context, "", "");
                }
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

    public static void HandleActivityFetching(Context context, String email, String password,
                                                             final VolleyCallback volleyCallback) {
        type = "FetchMyActivities";
        final ArrayList<Activity> data = new ArrayList<>();
        InitVolleyAndMoshi(context);
        // sending the user json here for simplicity
        JsonAdapter<User> adapter = moshi.adapter(User.class);
        User currentUser = new User(email, password);
        currentUser.setToken(SharedPrefsHandler.GetUserToken(context));
        StringBuilder updatedURL = new StringBuilder(baseURL).append("activities/me");
        MakeRequestGet(context, updatedURL, new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<Activity> result) {
                data.addAll((ArrayList<Activity>) result);
                volleyCallback.onSuccess(data);
            }

            @Override
            public void onError(ArrayList<Activity> result) {
                Log.d(TAG, "onError: Something went wrong");
            }
        });
    }

    // this method handles all get requests!
    public static void MakeRequestGet(Context context, StringBuilder updatedURL, final VolleyCallback volleyCallback) {
        ArrayList<Activity> result = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, updatedURL.toString(), null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("activities");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String base64ImageString = jsonObject.getString("base64ImageString");
                            String date = jsonObject.getString("date");
                            String startTime = jsonObject.getString("startTime");
                            String endTime = jsonObject.getString("endTime");
                            String address = jsonObject.getString("address");
                            Activity activity = new Activity(name, base64ImageString, date, startTime, endTime, address);
                            result.add(activity);
                        }
                        volleyCallback.onSuccess(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.d(TAG, "MakeRequestGet: " + error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", SharedPrefsHandler.GetUserToken(context));
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonObjectRequest);
    }
}
