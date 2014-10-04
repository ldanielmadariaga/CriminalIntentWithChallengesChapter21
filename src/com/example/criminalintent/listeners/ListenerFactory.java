package com.example.criminalintent.listeners;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextWatcher;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker.OnDateChangedListener;

import com.example.criminalintent.Crime;

public class ListenerFactory {

	public static TextWatcher getCrimeTextChangeListener(Crime crime) {
		return CrimeTextWatcher.getInstance(crime);
	}

	public static OnCheckedChangeListener getCrimeSolvedCheckboxListener(Crime crime) {
		return CrimeSolvedCheckBoxListener.getInstance(crime);
	}

	public static OnPageChangeListener getCrimesPageChangeListener(Context context, ArrayList<Crime> crimes) {
		return CrimesPageChangeListener.getInstance(context, crimes);
	}

	public static android.view.View.OnClickListener getCrimeDateOnClickListener(FragmentManager fragmentManager,
			Fragment fragment, Date crimeDate) {
		return CrimeDateOnClickListener.getInstance(fragmentManager, fragment, crimeDate);
	}

	public static android.content.DialogInterface.OnClickListener getDatePositiveButtonOnClickListener(
			Fragment fragment) {
		return DatePositiveButtonOnClickListener.getInstance(fragment);
	}

	public static OnDateChangedListener getCrimeDateOnDateChangedListener(Bundle arguments) {
		return CrimeDateOnDateChangedListener.getInstance(arguments);
	}

	public static MultiChoiceModeListener getCrimesMultiChoiceModeListener(ListFragment listFragment) {
		return DeleteCrimesMultiChoiceModeListener.getInstance(listFragment);
	}

	public static PictureButtonListener getPictureButtonListener(Activity activity) {
		return PictureButtonListener.getInstance(activity);
	}
}
