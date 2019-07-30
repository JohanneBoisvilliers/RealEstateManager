package com.openclassrooms.realestatemanager.utils;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    public static Boolean isInit = false;
    public static Application instance;

    public MyApp() {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getBaseContext();
    }
}