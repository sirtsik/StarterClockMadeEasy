package com.laube.sirje.starterclockmadeeasy;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

public class DayNightModeActivity extends AppCompatActivity {

    public static Boolean isDayMode = true;

    public void switchDayNightMode() {
        if (isDayMode) {
            isDayMode = false;
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            isDayMode = true;
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        Log.d("SIRJE", "Value of isDayMode: " + isDayMode);
    }
}
