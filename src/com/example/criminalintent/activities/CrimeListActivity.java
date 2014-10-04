package com.example.criminalintent.activities;

import android.support.v4.app.Fragment;

import com.example.criminalintent.fragments.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActtivity {

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}

}
