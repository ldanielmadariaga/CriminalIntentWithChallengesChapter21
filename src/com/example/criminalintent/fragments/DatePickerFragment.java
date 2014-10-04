package com.example.criminalintent.fragments;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.example.criminalintent.R;
import com.example.criminalintent.listeners.ListenerFactory;

public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_DATE = "com.example.criminalintent.date";
	private Date crimeDate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

		crimeDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar = createCalendarFromDate(crimeDate);

		DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);

		OnDateChangedListener onDateChangedListener = ListenerFactory
				.getCrimeDateOnDateChangedListener(getArguments());
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), onDateChangedListener);

		OnClickListener onClickListener = ListenerFactory.getDatePositiveButtonOnClickListener(this);
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(view)
				.setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, onClickListener)
				.create();

		return alertDialog;
	}

	public static DatePickerFragment newInstance(Date date) {
		Bundle arguments = new Bundle();
		arguments.putSerializable(EXTRA_DATE, date);

		DatePickerFragment datePickerFragment = new DatePickerFragment();
		datePickerFragment.setArguments(arguments);

		return datePickerFragment;
	}

	private Calendar createCalendarFromDate(Date crimeDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(crimeDate);
		return calendar;
	}
}
