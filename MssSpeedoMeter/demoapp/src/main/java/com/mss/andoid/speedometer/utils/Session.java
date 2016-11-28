package com.mss.andoid.speedometer.utils;


import com.mss.andoid.speedometer.interfaces.OnUpdateSpeed;

public class Session {
    private static OnUpdateSpeed sUpdateCheckBox;

    public static void getUpdateSpeed(float speed) {

        if (sUpdateCheckBox != null) {
            sUpdateCheckBox.onUpdateSpeedKm(speed);
        }
    }

    public static void setsUpdateSpeed(OnUpdateSpeed listner) {
        if (listner != null) {
            sUpdateCheckBox = listner;
        }

    }
}
