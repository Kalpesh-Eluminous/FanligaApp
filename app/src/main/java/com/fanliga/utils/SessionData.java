package com.fanliga.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionData {

    public SharedPreferences.Editor editor;
    public SharedPreferences pref;
    public static String EMPTY_STRING = "";

    /**
     * This is constructor
     *
     * @param context
     */
    public SessionData(Context context) {
        pref = context.getSharedPreferences("FanLiga", 0);
        editor = pref.edit();
    }

    /**
     * This is used to add Object as JSON String into shared preferences
     *
     * @param key
     * @param value
     */
    public void setObjectAsString(String key, String value) {
      //  Log.d(key, value);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * This is used to get Object as JSON String from preferences
     *
     * @param key
     * @return
     */
    public String getObjectAsString(String key) {
        return pref.getString(key, EMPTY_STRING);
    }

}
