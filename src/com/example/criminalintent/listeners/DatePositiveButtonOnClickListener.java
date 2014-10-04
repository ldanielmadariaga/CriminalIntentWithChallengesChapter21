package com.example.criminalintent.listeners;

import java.util.Date;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.criminalintent.fragments.DatePickerFragment;

public class DatePositiveButtonOnClickListener implements OnClickListener {

	private Fragment fragment;

	private DatePositiveButtonOnClickListener(Fragment fragment) {
		this.fragment = fragment;
	}

	public static DatePositiveButtonOnClickListener getInstance(Fragment fragment) {
		return new DatePositiveButtonOnClickListener(fragment);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		sendResult(Activity.RESULT_OK);
	}

	private void sendResult(int resultCode) {
		Intent intent = new Intent();
		intent.putExtra(DatePickerFragment.EXTRA_DATE,
				(Date) fragment.getArguments().getSerializable(DatePickerFragment.EXTRA_DATE));
		fragment.getTargetFragment().onActivityResult(fragment.getTargetRequestCode(), resultCode, intent);
	}
}
