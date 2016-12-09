package com.mss.andoid.speedometer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class AppPreferences {
    SharedPreferences pref;
    Editor editor;
    public static Context mContext;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "stepcounts_prefers";
    private static final AppPreferences ourInstance = new AppPreferences();


    public AppPreferences() {

    }

    @SuppressLint("CommitPrefEdits")
    public AppPreferences(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static void init(Context context) {
        AppPreferences.mContext = context;

    }

    public static AppPreferences getInstance() {
        return ourInstance;
    }





    public String getPrefrenceString(String keyName) {
        return pref.getString(keyName, "");
    }

    public void setPrefrenceString(String keyName, String stringValue) {
        editor.putString(keyName, stringValue);
        editor.commit();
    }



}
