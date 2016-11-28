package com.mss.andoid.speedometer.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mss.andoid.speedometer.utils.AppController;
import com.mss.andoid.speedometer.utils.Session;

import static android.location.LocationManager.GPS_PROVIDER;


public class Nmeaservice extends Service implements LocationListener ,  ActivityCompat.OnRequestPermissionsResultCallback{

    protected LocationManager locationManager;
    private Context mContext;
    double lat = 0.0, lag = 0.0;
    boolean fast = false;
    final private int REQUEST_CODE_LOCATION = 124;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.mContext = this;
        getLoactionData();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                    if (locationManager != null) {
                        locationManager.removeUpdates(Nmeaservice.this);
                    }
                }
            } else {

                if (locationManager != null) {
                    locationManager.removeUpdates(Nmeaservice.this);
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public float convertSpeedKontTo(String sp) {

        float speed = 0;
        if (!sp.isEmpty()) {
            speed = (float) (Double.parseDouble(sp) * 1.85);
        }
        return speed;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getLoactionData();
                } else {
                    // Permission Denied

                }
                break;
        }
    }

    private void getLoactionData() {

        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager
                .isProviderEnabled(GPS_PROVIDER);


        if (!isGPSEnabled && !isNetworkEnabled) {

        } else {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (Build.VERSION.SDK_INT >= 23) {
                int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AppController.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION);

                    // Update location when change 5 meter or 5 sec
                    locationManager.requestLocationUpdates(GPS_PROVIDER, 1000 * 5, 5, this);

                } else {
                    // Update location when change 5 meter or 5 sec
                    locationManager.requestLocationUpdates(GPS_PROVIDER, 1000 * 5, 5, this);
                }
            } else {
                if (isGPSEnabled) {
                    // Update location when change 5 meter or 5 sec
                    locationManager.requestLocationUpdates(GPS_PROVIDER, 1000 * 5, 5, this);
                }
            }

            //nmea Listener add using gpsStatus get nmea sentence
            locationManager.addNmeaListener(new GpsStatus.NmeaListener() {
                @Override
                public void onNmeaReceived(long timestamp, String nmea) {
                    //split ',' and get speed in kont
                    String[] mstr = nmea.split(",");

                    if (mstr[0].equals("$GPRMC")) {
                        if (mstr[2].equals("A")) {
                            //set Speed on speedometer Ui by nmea sentence structure if available (speed not zero)
                            //convert kont to Killo meter
                            Session.getUpdateSpeed(convertSpeedKontTo(mstr[7]));
                        }else {
                            //set Speed on speedometer Ui by nmea sentence structure if  invalid (speed Zero)
                            //convert kont to Killo meter
                            Session.getUpdateSpeed(convertSpeedKontTo(mstr[7]));
                        }
                        }
                }
            });

        }


    }




}
