package com.example.criminalintent.activities;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.criminalintent.Crime;
import com.example.criminalintent.CrimeLab;
import com.example.criminalintent.R;
import com.example.criminalintent.adapters.AdapterFactory;
import com.example.criminalintent.fragments.CrimeFragment;
import com.example.criminalintent.listeners.ListenerFactory;

public class CrimePagerActivity extends FragmentActivity {

	private ViewPager viewPager;
	private ArrayList<Crime> crimes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.view_pager);
		setContentView(viewPager);

		crimes = CrimeLab.getInstance(this).getCrimes();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentPagerAdapter fragmentPagerAdapter = AdapterFactory.getCrimeFragmentPagerAdapter(fragmentManager,
				crimes);
		viewPager.setAdapter(fragmentPagerAdapter);
		OnPageChangeListener onPageChangeListener = ListenerFactory.getCrimesPageChangeListener(this, crimes);
		viewPager.setOnPageChangeListener(onPageChangeListener);
		loadSelectedCrime();

	}

	private void loadSelectedCrime() {

		UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (Crime crime : crimes) {
			if (crime.getId().equals(crimeId)) {
				viewPager.setCurrentItem(crimes.indexOf(crime));
				// TODO Remove?
				break;
			}
		}
	}
}
