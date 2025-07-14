package vn.edu.fpt.prm.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.features.user.User;

public class AuthManager {
    private static final String PREF_NAME = "auth_pref";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";

    public static void saveAuth(Context context, String token, User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER, userJson)
                .apply();

        Logger.debug("AuthManager", "Saved token: " + token + ", user: " + userJson);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public static User getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson == null) return null;
        return new Gson().fromJson(userJson, User.class);
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
