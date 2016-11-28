package com.mss.andoid.speedometer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.mss.andoid.speedometer.utils.AppController;
import com.mss.andoid.speedometer.services.Nmeaservice;
import com.mss.andoid.speedometer.R;
import com.mss.andoid.speedometer.utils.Session;
import com.mss.andoid.speedometer.interfaces.OnUpdateSpeed;


public class MainActivity extends AppCompatActivity implements OnUpdateSpeed {
    private SpeedometerGauge mSpeedometer;
    private Intent mIntentNmeaService;
    private TextView txtSpeedKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
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

}
