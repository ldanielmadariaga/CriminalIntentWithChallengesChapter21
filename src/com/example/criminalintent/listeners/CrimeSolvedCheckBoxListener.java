package com.example.criminalintent.listeners;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.criminalintent.Crime;

class CrimeSolvedCheckBoxListener implements OnCheckedChangeListener {

	private static CrimeSolvedCheckBoxListener crimeSolvedCheckBoxListener;
	private Crime crime;

	private CrimeSolvedCheckBoxListener(Crime crime) {
		this.crime = crime;
	}

	public static CrimeSolvedCheckBoxListener getInstance(Crime crime) {
		crimeSolvedCheckBoxListener = new CrimeSolvedCheckBoxListener(crime);
		return crimeSolvedCheckBoxListener;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		crime.setSolved(isChecked);
	}

}
