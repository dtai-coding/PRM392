package com.example.phoneapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
