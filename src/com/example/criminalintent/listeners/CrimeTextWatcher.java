package com.example.criminalintent.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.criminalintent.Crime;

class CrimeTextWatcher implements TextWatcher {

	private Crime crime;
	private static CrimeTextWatcher crimeTextWatcher;

	private CrimeTextWatcher(Crime crime) {
		this.crime = crime;
	}

	public static CrimeTextWatcher getInstance(Crime crime) {
		crimeTextWatcher = new CrimeTextWatcher(crime);
		return crimeTextWatcher;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		crime.setTitle(s.toString());
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
