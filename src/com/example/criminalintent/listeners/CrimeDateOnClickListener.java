package com.example.criminalintent.listeners;

import java.util.Date;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.criminalintent.fragments.CrimeFragment;
import com.example.criminalintent.fragments.DatePickerFragment;

class CrimeDateOnClickListener implements OnClickListener {

	private static final String DIALOG_DATE = "date";

	private FragmentManager fragmentManager;
	private Fragment fragment;
	private Date crimeDate;

	private CrimeDateOnClickListener(FragmentManager fragmentManager, Fragment fragment, Date crimeDate) {
		this.fragmentManager = fragmentManager;
		this.fragment = fragment;
		this.crimeDate = crimeDate;
	}

	public static OnClickListener getInstance(FragmentManager fragmentManager, Fragment fragment, Date crimeDate) {
		return new CrimeDateOnClickListener(fragmentManager, fragment, crimeDate);
	}

	@Override
	public void onClick(View v) {
		DatePickerFragment dialog = DatePickerFragment.newInstance(crimeDate);
		dialog.setTargetFragment(fragment, CrimeFragment.REQUEST_DATE);
		dialog.show(fragmentManager, DIALOG_DATE);
	}
}
