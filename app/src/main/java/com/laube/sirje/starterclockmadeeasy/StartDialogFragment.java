package com.laube.sirje.starterclockmadeeasy;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.text.Format;
import java.util.Calendar;

public class StartDialogFragment extends DialogFragment {
    private static final String KEY_BOOLEAN_IS_INTERVAL_START = "isIntervalStart";
    private static final String KEY_IS_DAY_MODE = "isDayMode";
    NumberPicker minutesPicker;
    NumberPicker secondsPicker;
    TimePicker startTimePicker;
    Button confirmButton;
    Button cancelButton;
    TextView intervalPickerLabel;
    LinearLayout intervalPickerContainer;
    LinearLayout intervalPickerContainerMain;

    int hourPicked;
    int minutePicked;
    int year;
    int month;
    int day;
    private long intervalSet;
    long intervalSecondSet;
    long intervalMinuteSet;
    int intervalMinutePicked;
    int intervalSecondPicked;
    private long nextStartTimeMS;
    Boolean isIntervalStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);

        Bundle bundle = this.getArguments();
        isIntervalStart = bundle.getBoolean(KEY_BOOLEAN_IS_INTERVAL_START);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_dialog_fragment, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        minutesPicker = getView().findViewById(R.id.minutes_picker);
        secondsPicker = getView().findViewById(R.id.seconds_picker);
        startTimePicker = getView().findViewById(R.id.start_time_picker);
        confirmButton = getView().findViewById(R.id.button_confirm_dialog);
        cancelButton = getView().findViewById(R.id.button_cancel_dialog);
        intervalPickerContainerMain = getView().findViewById(R.id.interval_picker_container_main);
        intervalPickerLabel = getView().findViewById(R.id.pick_interval_label);
        intervalPickerContainer = getView().findViewById(R.id.interval_picker_container);

        Calendar c = Calendar.getInstance();

        startTimePicker.setIs24HourView(true);
        startTimePicker.setMinute(c.get(Calendar.MINUTE)+1);

        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(60);

        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);

        if(!isIntervalStart) {
            intervalPickerContainerMain.setVisibility(View.GONE);
            confirmButton.setEnabled(true);
        } else {
            confirmButton.setEnabled(false);
            secondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if (secondsPicker.getValue()*1000 + minutesPicker.getValue()*60000 <= 1000) {
                        confirmButton.setEnabled(false);
                    } else {
                        confirmButton.setEnabled(true);
                    }
                }
            });
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourPicked = startTimePicker.getHour();
                minutePicked = startTimePicker.getMinute();
                intervalMinutePicked = minutesPicker.getValue();
                intervalSecondPicked = secondsPicker.getValue();
                nextStartTimeMS = getTimeSet();
                intervalSet = getIntervalSet();

                if (nextStartTimeMS < System.currentTimeMillis()) {
                    nextStartTimeMS = nextStartTimeMS + 86400000;
                }

                Intent intent = new Intent(getActivity(), StartActivity.class);
                intent.putExtra("ISINTERVALSTART", isIntervalStart);
                intent.putExtra("NEXTSTARTTIMEMS", nextStartTimeMS);


                if (isIntervalStart) {
                    intent.putExtra("INTERVAL", intervalSet);
                }

                startActivity(intent);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public long getTimeSet() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(year, month, day, hourPicked, minutePicked, 0);

        return calendar.getTimeInMillis();
    }

    public long getIntervalSet() {
        intervalSecondSet = intervalSecondPicked*1000;
        intervalMinuteSet = intervalMinutePicked*60000;
        intervalSet = intervalMinuteSet + intervalSecondSet;

        return intervalSet;
    }
}
