package com.plugin.datepicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog {

	private TimePicker timePicker;
	private final OnTimeSetListener callback;

	private final int minuteInterval;

	public CustomTimePickerDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView, int minuteInterval) {
		this(context, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, callBack, hourOfDay, minute,
				is24HourView, minuteInterval);
	}

	public CustomTimePickerDialog(Context context, int theme, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView, int minuteInterval) {
		super(context, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, callBack, hourOfDay, minute / (minuteInterval == 0 ? 1 : minuteInterval),
				is24HourView);
		if (minuteInterval <= 0 || 30 < minuteInterval)
			minuteInterval = 1;
		this.callback = callBack;
		this.minuteInterval = minuteInterval;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (callback != null && timePicker != null) {
			timePicker.clearFocus();
			callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
					timePicker.getCurrentMinute() * this.minuteInterval);
		}
	}

	@Override
	protected void onStop() {
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		try {
			Class<?> classForid = Class.forName("com.android.internal.R$id");
			Field timePickerField = classForid.getField("timePicker");
			this.timePicker = (TimePicker) findViewById(timePickerField
					.getInt(null));
			Field field = classForid.getField("minute");

			NumberPicker mMinuteSpinner = (NumberPicker) timePicker
					.findViewById(field.getInt(null));
			mMinuteSpinner.setMinValue(0);
			mMinuteSpinner.setMaxValue((60 / this.minuteInterval) - 1);
			List<String> displayedValues = new ArrayList<String>();
			for (int i = 0; i < 60; i += this.minuteInterval) {
				displayedValues.add(String.format("%02d", i));
			}
			mMinuteSpinner.setDisplayedValues(displayedValues
					.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
