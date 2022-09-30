package com.hero.seoultechteams;

import android.app.Application;

public class SeoultechTeamsApp extends Application {
        
    private static Application instance;
        
    @Override
    public void onCreate() {
        super.onCreate();
        SeoultechTeamsApp.instance = this;
    }
        
    public static Application getInstance() {
            return instance;
        }
}