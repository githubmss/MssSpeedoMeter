package com.mss.andoid.speedometer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.mss.andoid.speedometer.R;
import com.mss.andoid.speedometer.interfaces.OnUpdateSpeed;
import com.mss.andoid.speedometer.services.GPSTracker;
import com.mss.andoid.speedometer.services.Nmeaservice;
import com.mss.andoid.speedometer.utils.AppController;
import com.mss.andoid.speedometer.utils.AppPreferences;
import com.mss.andoid.speedometer.utils.Constants;
import com.mss.andoid.speedometer.utils.Session;


public class MainActivity extends AppCompatActivity implements OnUpdateSpeed {
    private SpeedometerGauge mSpeedometer;
    private Intent mIntentNmeaService;
    private TextView txtSpeedKm;
    Toolbar toolbar;
    Context mContext;
    private GPSTracker gps;
    private AppPreferences mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("MssSpeedoMeter");
        mSession = new AppPreferences(this);
        ImageView imgIcon = (ImageView) findViewById(R.id.mss);
        imgIcon.setImageResource(R.drawable.app_icon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            gps = new GPSTracker(mContext, MainActivity.this);
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                mSession.setPrefrenceString(Constants.CURRENT_LATITUDE, "" + latitude);
                double longitude = gps.getLongitude();
                mSession.setPrefrenceString(Constants.CURRENT_LONGITUDE, "" + longitude);
            } else {
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.contact_us) {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.about_us) {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUi() {
        AppController.activity = MainActivity.this;
        // Customize SpeedometerView
        mSpeedometer = (SpeedometerGauge) findViewById(R.id.speedometer);
        txtSpeedKm = (TextView) findViewById(R.id.txt_km);
        Session.setsUpdateSpeed(this);
        //start service
        mIntentNmeaService = new Intent(MainActivity.this, Nmeaservice.class);
        startService(mIntentNmeaService);
        Session.setsUpdateSpeed(this);
        populateUi();
    }

    private void populateUi() {
        // Add label converter
        mSpeedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        txtSpeedKm.setText(getResources().getString(R.string.txt_speed));
        // configure value max range
        mSpeedometer.setMaxSpeed(120);
        // configure value MajorTickStep
        mSpeedometer.setMajorTickStep(10);
        // configure value Ticks
        mSpeedometer.setMinorTicks(10);
        // Configure value range colors
        mSpeedometer.addColoredRange(30, 60, Color.GREEN);
        mSpeedometer.addColoredRange(60, 80, Color.YELLOW);
        mSpeedometer.addColoredRange(80, 120, Color.RED);
        //set speed
        mSpeedometer.setSpeed(1, 1000, 120);
    }

    @Override
    public void onUpdateSpeedKm(float speed) {
        // Update speed in km by nmea
        int speedKm = (int) speed;
        mSpeedometer.setSpeed(speedKm, 1000, 120);
        txtSpeedKm.setText(getResources().getString(R.string.txt_speed_only) + speedKm + " " + getResources().getString(R.string.txt_km));
    }

    @Override
    protected void onDestroy() {
        //stop service
        stopService(mIntentNmeaService);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gps = new GPSTracker(mContext, MainActivity.this);
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        mSession.setPrefrenceString(Constants.CURRENT_LATITUDE, "" + latitude);
                        double longitude = gps.getLongitude();
                        mSession.setPrefrenceString(Constants.CURRENT_LONGITUDE, "" + longitude);
                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
                }
                return;
            }
        }
    }
}
