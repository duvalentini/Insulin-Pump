package com.example.gestalt.insulinpumpulator;

import android.util.Log;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.signin.SignInManager;


/**
 * Created by Joshua on 6/4/2016.
 */
public class Initializer extends MultiDexApplication {

    private final static String LOG_TAG = Initializer.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        initializeApplication();
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");
    }

    private void initializeApplication() {
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());

        // ...Put any application-specific initialization logic here...
    }
}
