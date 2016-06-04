package com.visiontech.yummysmile.models;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 *
 * This class will handle all values we need to store in shared preferences.
 *
 * @author manuel.ortiz on 30/05/16.
 */
public class YummySharedPreferences {
    private static final String USER_SHARED_PREF = "USER_SHARED_PREF";
    private static final String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    private final Context context;

    @Inject
    public YummySharedPreferences(Context context) {
        this.context = context;
    }

    /**
     * Set the email that the user selected to be remembered
     * @param email Email to be stored. Set empty string if you want to remove it.
     */
    public void setEmail(String email) {
        SharedPreferences.Editor editor = getEditor(USER_SHARED_PREF);
        editor.putString(USER_EMAIL_KEY, email);
        editor.commit();
    }

    /**
     * Get the email that the user selected to be remembered
     * @return The email stored
     */
    public String getEmail() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);

        return sharedPreferences.getString(USER_EMAIL_KEY, "");
    }

    private SharedPreferences.Editor getEditor(String sharedPreferencesFile) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE);

        return sharedPreferences.edit();
    }
}
