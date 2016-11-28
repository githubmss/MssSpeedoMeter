package com.mss.andoid.speedometer.utils;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


public class AppController extends MultiDexApplication {


	public  static Activity activity;
	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
	}


	public static Context mainActivity;



}
