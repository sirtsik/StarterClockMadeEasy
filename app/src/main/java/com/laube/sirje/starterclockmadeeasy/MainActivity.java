package com.laube.sirje.starterclockmadeeasy;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends DayNightModeActivity {

    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTES = "minutes";
    private static final String KEY_BOOLEAN_IS_INTERVAL_START = "isIntervalStart";
    private static final String KEY_IS_NIGHT_MODE = "isDayMode";
    Boolean isIntervalStart;
    Button navigateToMassStartActivity;
    Button navigateToIntervalStartActivity;
    int hourPicked;
    int minutePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        navigateToMassStartActivity = findViewById(R.id.btn_navigate_to_mass_start);
        navigateToIntervalStartActivity = findViewById(R.id.btn_navigate_to_interval_start);

        if (savedInstanceState != null) {
            hourPicked = savedInstanceState.getInt(KEY_HOUR);
            minutePicked = savedInstanceState.getInt(KEY_MINUTES);
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isNightMode = preferences.getBoolean(KEY_IS_NIGHT_MODE, isDayMode);

            if (isNightMode) {
                switchDayNightMode();
            }
        }
    }

    public void btnNavigateToMassStartActivity(View view) {
        isIntervalStart = false;
        pickStartTime();
    }

    public void btnNavigateToIntervalStartActivity(View view) {
        isIntervalStart = true;
        pickStartTime();
    }

    public void pickStartTime() {
        StartDialogFragment startDialogFragment = new StartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_BOOLEAN_IS_INTERVAL_START, isIntervalStart);
        startDialogFragment.setArguments(bundle);
        startDialogFragment.show(getSupportFragmentManager(), "SwagTag");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_HOUR, hourPicked);
        outState.putInt(KEY_MINUTES, minutePicked);
    }

    public void btnSwitchBetweenModes(View view) {
        switchDayNightMode();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean(KEY_IS_NIGHT_MODE, !isDayMode).apply();
    }
}
