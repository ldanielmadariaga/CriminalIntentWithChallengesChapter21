package com.example.criminalintent.listeners;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class PictureButtonListener implements OnClickListener {

	private Activity activity;

	public PictureButtonListener(Activity activity) {
		this.activity = activity;
	}

	public static PictureButtonListener getInstance(Activity activity) {
		return new PictureButtonListener(activity);
	}

	@Override
	public void onClick(View view) {
		activity.finish();
	}
}
