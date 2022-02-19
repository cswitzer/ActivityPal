package com.example.activitypal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefsHandler {
    private static final String MESSAGE_ID = "user_cred";

    public static void SaveUserCred(Context context, String email, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    public static Pair<String, String> GetCredPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");
        return new Pair<>(email, password);
    }
}
