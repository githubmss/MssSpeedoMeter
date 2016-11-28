package com.mss.andoid.speedometer.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.mss.andoid.speedometer.R;

public class SplashActivity extends Activity {
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;
    private static final int REQUEST_READ_EXTERNALSTORAGE_PERMISSION = 226;
    private static final int REQUEST_WRITE_EXTERNALSTORAGE_PERMISSION = 227;
    private static final int TARGETSD_VERSION_CODE=23;
    private String TAG = SplashActivity.class.getSimpleName();
    private SplashActivity ctx;
    private long DELAY_COUNT=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = SplashActivity.this;
        if (Build.VERSION.SDK_INT >= TARGETSD_VERSION_CODE) {
            checkPermission();
        } else {
            intUi();
        }
    }

    private void intUi() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, DELAY_COUNT);
    }
    public void checkPermission() {
        //get all required elements
        int ACCESS_COARSE_LOCATION_PREMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int ACCESS_FINE_LOCATION_PREMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        //Check permissions if permission already granted run if blog if not granted then run else blog
        if (ACCESS_COARSE_LOCATION_PREMISSION == PackageManager.PERMISSION_GRANTED && ACCESS_FINE_LOCATION_PREMISSION == PackageManager.PERMISSION_GRANTED) {
            intUi();
        } else {
            ActivityCompat.requestPermissions(ctx, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_READ_EXTERNALSTORAGE_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case REQUEST_READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intUi();
                }
                return;
            }
            case REQUEST_READ_EXTERNALSTORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    intUi();
                }
                return;
            }
            case REQUEST_WRITE_EXTERNALSTORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    intUi();
                }
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}