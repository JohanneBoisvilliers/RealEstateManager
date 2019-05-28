package com.openclassrooms.realestatemanager.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApp extends Application {

    public static Boolean isInit = false;
    public static Application instance;

    public MyApp() {

        Log.i("main", "Constructor fired");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Log.i("main", "onCreate fired");
    }

    public static Context getContext() {
        return instance.getBaseContext();
    }
}