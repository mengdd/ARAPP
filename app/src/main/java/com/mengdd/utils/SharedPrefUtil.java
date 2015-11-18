package com.mengdd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPrefUtil {

    private static final String PREFERENCES_NAME = "meng_pref";
    private static final String LOG_TAG = "shared_pref";

    public static void saveLong(Context context, String key, long l) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        editor.putLong(key, l);
        editor.commit();
    }

    public static long getLong(Context context, String key, long defaultlong) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        return pref.getLong(key, defaultlong);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key,
            boolean defaultboolean) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultboolean);
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public static int getInt(Context context, String key, int defaultInt) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return pref.getInt(key, defaultInt);
    }

    public static void saveString(Context context, String key, String value) {
        Log.i(LOG_TAG, "saveString: " + key + " : " + value);

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key,
            String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }

}
