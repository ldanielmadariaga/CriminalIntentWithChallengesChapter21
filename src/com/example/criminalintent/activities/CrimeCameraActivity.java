package com.example.criminalintent.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.example.criminalintent.fragments.CrimeCameraFragment;

public class CrimeCameraActivity extends SingleFragmentActtivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		return new CrimeCameraFragment();
	}

}
