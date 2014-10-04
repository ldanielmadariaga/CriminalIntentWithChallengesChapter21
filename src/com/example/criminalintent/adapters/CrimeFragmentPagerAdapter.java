package com.example.criminalintent.adapters;

import java.util.ArrayList;
import java.util.UUID;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.criminalintent.Crime;
import com.example.criminalintent.fragments.CrimeFragment;

class CrimeFragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Crime> crimes;

	private CrimeFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Crime> crimes) {
		super(fragmentManager);
		this.crimes = crimes;
	}

	public static CrimeFragmentPagerAdapter getInstance(FragmentManager fragmentManager,
			final ArrayList<Crime> crimes) {
		return new CrimeFragmentPagerAdapter(fragmentManager, crimes);
	}

	@Override
	public int getCount() {
		return crimes.size();
	}

	@Override
	public Fragment getItem(int position) {
		Crime crime = crimes.get(position);
		UUID crimeId = crime.getId();
		return CrimeFragment.newInstance(crimeId);
	}
}
