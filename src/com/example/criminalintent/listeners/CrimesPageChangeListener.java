package com.example.criminalintent.listeners;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.criminalintent.Crime;
import com.example.criminalintent.activities.CrimePagerActivity;

class CrimesPageChangeListener implements OnPageChangeListener {

	private Context context;
	private ArrayList<Crime> crimes;

	private CrimesPageChangeListener(Context context, ArrayList<Crime> crimes) {
		this.context = context;
		this.crimes = crimes;
	}

	public static CrimesPageChangeListener getInstance(Context context, ArrayList<Crime> crimes) {
		return new CrimesPageChangeListener(context, crimes);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		Crime crime = crimes.get(position);
		if (crime.getTitle() != null) {
			((CrimePagerActivity) context).setTitle(crime.getTitle());
		}
	}
}
