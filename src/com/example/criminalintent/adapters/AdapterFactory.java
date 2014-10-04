package com.example.criminalintent.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.example.criminalintent.Crime;

public class AdapterFactory {

	public static CrimeListAdapter getCrimeListAdapter(Context context, ArrayList<Crime> crimes) {
		return CrimeListAdapter.getInstance(context, crimes);
	}

	public static CrimeFragmentPagerAdapter getCrimeFragmentPagerAdapter(FragmentManager fragmentManager,
			ArrayList<Crime> crimes) {
		return CrimeFragmentPagerAdapter.getInstance(fragmentManager, crimes);
	}

}
