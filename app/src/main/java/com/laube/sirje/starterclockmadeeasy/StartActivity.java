package com.laube.sirje.starterclockmadeeasy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StartActivity extends DayNightModeActivity {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat simpleDateFormatWithoutHour = new SimpleDateFormat(":ss", Locale.getDefault());
    private static final String KEY_NEXT_START_TIME = "nextStartTimeMS";
    private static final String KEY_BOOLEAN_IS_INTERVAL_START = "isIntervalStart";
//    private static final String KEY_IS_DAY_MODE = "isDayMode";
    TextView tvMainClock;
    TextView tvNextStartClock;
    TextView countDownUntilStartClock;
    TextView competitorNumberDisplay;
    Handler handler;
    Runnable runnable;
    private long nextStartTimeMS;
    private long intervalSet;
    Boolean isIntervalStart;
    Boolean tenSecondBeepDone = false;
    Boolean fiveSecondBeepDone = false;
    Boolean fourSecondBeepDone = false;
    Boolean threeSecondBeepDone = false;
    Boolean twoSecondBeepDone = false;
    Boolean oneSecondBeepDone = false;
    Boolean startBeep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        tvMainClock = findViewById(R.id.tv_main_clock);
        tvNextStartClock = findViewById(R.id.tv_clock_next_start);
        countDownUntilStartClock = findViewById(R.id.tv_countdown_until_start_ms);
        competitorNumberDisplay = findViewById(R.id.tv_competitor_number);
        handler = new Handler();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null) {
            isIntervalStart = (boolean) b.get("ISINTERVALSTART");
            if (isIntervalStart) {
                intervalSet = (long) b.get("INTERVAL");
            }
            nextStartTimeMS = (long) b.get("NEXTSTARTTIMEMS");
        }


        countDownUntilStartClock.setVisibility(View.INVISIBLE);
        countDownUntilStartClock.setText(simpleDateFormatWithoutHour.format(new Date(0)));
        tvNextStartClock.setText(simpleDateFormat.format(nextStartTimeMS));

        MasterTick();

        if(savedInstanceState != null) {
            nextStartTimeMS = savedInstanceState.getLong(KEY_NEXT_START_TIME);
            isIntervalStart = savedInstanceState.getBoolean(KEY_BOOLEAN_IS_INTERVAL_START);
//            isDayMode = savedInstanceState.getBoolean(KEY_IS_DAY_MODE);
        }

    }

    public void MasterTick() {
        final ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                long nowTimeMS = System.currentTimeMillis();
                tvMainClock.setText(simpleDateFormat.format(nowTimeMS));

                long timeUntilNextStartMS = nextStartTimeMS - nowTimeMS;

                if (nextStartTimeMS != 0) {
                    if (nowTimeMS < nextStartTimeMS) {

                        if (timeUntilNextStartMS <= 30000) {
                            countDownUntilStartClock.setVisibility(View.VISIBLE);
                        }
                        if (timeUntilNextStartMS <= 10000 && !tenSecondBeepDone) {
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 1000);
                            tenSecondBeepDone = true;
                        } else if(timeUntilNextStartMS <= 5000 && !fiveSecondBeepDone) {
                            countDownUntilStartClock.setTextColor(Color.RED);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                            fiveSecondBeepDone = true;
                        } else if(timeUntilNextStartMS <= 4000 && !fourSecondBeepDone) {
                            countDownUntilStartClock.setTextColor(Color.RED);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                            fourSecondBeepDone = true;
                        } else if(timeUntilNextStartMS <= 3000 && !threeSecondBeepDone) {
                            countDownUntilStartClock.setTextColor(Color.RED);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                            threeSecondBeepDone = true;
                        } else if(timeUntilNextStartMS <= 2000 && !twoSecondBeepDone) {
                            countDownUntilStartClock.setTextColor(Color.RED);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                            twoSecondBeepDone = true;
                        } else if(timeUntilNextStartMS <= 1000 && !oneSecondBeepDone) {
                            countDownUntilStartClock.setTextColor(Color.RED);
                            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                            oneSecondBeepDone = true;
                        }
                        countDownUntilStartClock.setText(simpleDateFormatWithoutHour.format(new Date(timeUntilNextStartMS+1000)));
                    } else {
                        if(!startBeep) {
                            countDownUntilStartClock.setTextColor(Color.GREEN);
                            countDownUntilStartClock.setText(simpleDateFormatWithoutHour.format(new Date(0)));
                            toneGen1.startTone(ToneGenerator.TONE_DTMF_P, 1000);
                            startBeep = true;
                        } else if (nextStartTimeMS - nowTimeMS <= -1000) {
                            if(isIntervalStart) {
                                nextStartTimeMS = nextStartTimeMS + intervalSet;
                                tvNextStartClock.setText(simpleDateFormat.format(nextStartTimeMS));
                                countDownUntilStartClock.setTextColor(Color.BLACK);
                                resetBooleans();
                            } else {
                                countDownUntilStartClock.setVisibility(View.INVISIBLE);
                                nextStartTimeMS = 0;
                            }
                        }
                    }

                }
                handler.postDelayed(this, 16);
            }
        };
        handler.post(runnable);
    }

    public void resetBooleans() {
        tenSecondBeepDone = false;
        fiveSecondBeepDone = false;
        fourSecondBeepDone = false;
        threeSecondBeepDone = false;
        twoSecondBeepDone = false;
        oneSecondBeepDone = false;
        startBeep = false;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_NEXT_START_TIME, nextStartTimeMS);
        outState.putBoolean(KEY_BOOLEAN_IS_INTERVAL_START, isIntervalStart);
//        outState.putBoolean(KEY_IS_DAY_MODE, isDayMode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        EndDialogFragment endDialogFragment = new EndDialogFragment();
        endDialogFragment.show(getSupportFragmentManager(), "endDialog");
    }
}
