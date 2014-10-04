package com.example.criminalintent.listeners;

import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.example.criminalintent.fragments.DatePickerFragment;

class CrimeDateOnDateChangedListener implements OnDateChangedListener {

	Bundle arguments;

	private CrimeDateOnDateChangedListener(Bundle arguments) {
		this.arguments = arguments;
	}

	public static CrimeDateOnDateChangedListener getInstance(Bundle arguments) {
		return new CrimeDateOnDateChangedListener(arguments);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Date crimeDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
		arguments.putSerializable(DatePickerFragment.EXTRA_DATE, crimeDate);

	}
}
