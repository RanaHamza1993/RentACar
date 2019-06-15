package com.team.rentacar.java;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;

public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
