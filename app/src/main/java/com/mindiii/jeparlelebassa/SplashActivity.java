package com.mindiii.jeparlelebassa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mindiii.jeparlelebassa.helper.SessionManager;

import io.fabric.sdk.android.Fabric;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SpleshActivity";
    private Handler handler = new Handler();
    private Runnable runnable;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_spesh);
        int SPLASH_TIME_OUT = 1500;
       sessionManager = new SessionManager(SplashActivity.this);

        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {

                if (sessionManager.isLoggedIn()) {

                    Intent  intent = new Intent(SplashActivity.this, MainActivity.class);
                    sessionManager.setlanguage(sessionManager.getLanguage());
                    startActivity(intent);
                    finish();

                } else {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    sessionManager.setlanguage(sessionManager.getLanguage());
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }



}

