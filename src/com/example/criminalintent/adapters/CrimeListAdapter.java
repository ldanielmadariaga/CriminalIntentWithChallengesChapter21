package com.example.criminalintent.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.criminalintent.Crime;
import com.example.criminalintent.R;
import com.example.criminalintent.activities.CrimeListActivity;

class CrimeListAdapter extends ArrayAdapter<Crime> {

	private Context context;

	private CrimeListAdapter(Context context, ArrayList<Crime> crimes) {
		super(context, 0, crimes);
		this.context = context;
	}

	public static CrimeListAdapter getInstance(Context context, ArrayList<Crime> crimes) {
		CrimeListAdapter crimeListAdapter = new CrimeListAdapter(context, crimes);
		return crimeListAdapter;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater layoutInflater = ((CrimeListActivity) context).getLayoutInflater();
			convertView = layoutInflater.inflate(R.layout.list_item_crime, null);
		}

		Crime crime = getItem(position);
		TextView titleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
		titleTextView.setText(crime.getTitle());
		TextView dateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
		dateTextView.setText(crime.getDate().toString());
		CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
		solvedCheckBox.setChecked(crime.isSolved());

		return convertView;

	}
}
