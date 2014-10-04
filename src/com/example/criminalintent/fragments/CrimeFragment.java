package com.example.criminalintent.fragments;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.criminalintent.Crime;
import com.example.criminalintent.CrimeLab;
import com.example.criminalintent.Photo;
import com.example.criminalintent.PictureUtils;
import com.example.criminalintent.R;
import com.example.criminalintent.activities.CrimeCameraActivity;
import com.example.criminalintent.listeners.ListenerFactory;

public class CrimeFragment extends Fragment {

	public static final String EXTRA_CRIME_ID = "com.example.criminalintent.crime_id";
	private static final String TAG = "CrimeFragment";
	public static final int REQUEST_DATE = 0;
	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_CONTACT = 2;
	private static final String DIALOG_IMAGE = "image";

	private Crime crime;
	private EditText titleField;
	private Button dateButton;
	private CheckBox solvedCheckBox;
	private ImageButton photoButton;
	private ImageView photoView;
	private Button suspectButton;
	private Button callButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
	}

	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle arguments = new Bundle();
		arguments.putSerializable(EXTRA_CRIME_ID, crimeId);

		CrimeFragment crimeFragment = new CrimeFragment();
		crimeFragment.setArguments(arguments);

		return crimeFragment;
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crime, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		setHasOptionsMenu(true);

		photoButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
		// TODO Extract to factory
		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivityForResult(intent, REQUEST_PHOTO);
			}
		});
		photoView = (ImageView) view.findViewById(R.id.crime_imageView);
		// TODO Move this
		photoView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Photo photo = crime.getPhoto();
				// TODO Down with flow control
				if (photo == null) {
					return;
				}
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				String filePath = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
				ImageFragment.getInstance(filePath).show(fragmentManager, DIALOG_IMAGE);
			}
		});

		PackageManager packageManager = getActivity().getPackageManager();
		boolean hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
				|| packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && android.hardware.Camera
						.getNumberOfCameras() > 0);
		if (!hasCamera) {
			photoButton.setEnabled(false);
		}

		titleField = (EditText) view.findViewById(R.id.crime_title);
		titleField.setText(crime.getTitle());
		titleField.addTextChangedListener(ListenerFactory.getCrimeTextChangeListener(crime));

		dateButton = (Button) view.findViewById(R.id.crime_date);
		updateDate();
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		OnClickListener onClickListener = ListenerFactory.getCrimeDateOnClickListener(fragmentManager,
				CrimeFragment.this, crime.getDate());
		dateButton.setOnClickListener(onClickListener);

		solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
		solvedCheckBox.setChecked(crime.isSolved());
		solvedCheckBox.setOnCheckedChangeListener(ListenerFactory.getCrimeSolvedCheckboxListener(crime));

		Button reportButton = (Button) view.findViewById(R.id.crime_reportButton);
		// TODO Move
		reportButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				intent = Intent.createChooser(intent, getString(R.string.send_report));
				startActivity(intent);
			}
		});

		suspectButton = (Button) view.findViewById(R.id.crime_suspectButton);
		suspectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, REQUEST_CONTACT);
			}
		});

		if (crime.getSuspect() != null) {
			suspectButton.setText(crime.getSuspect());
		}

		callButton = (Button) view.findViewById(R.id.crime_callSuspectButton);
		callButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (crime.getSuspectPhoneNumber() != null) {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + crime.getSuspectPhoneNumber()));
					// + crime.getSuspectPhoneNumber()));
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "This suspect does not have a phone number", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}

	private void showPhoto() {
		Photo photo = crime.getPhoto();
		BitmapDrawable bitmapDrawable = null;
		if (photo != null) {
			String filePath = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
			bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), filePath);
		}
		photoView.setImageDrawable(bitmapDrawable);
	}

	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.getInstance(getActivity()).saveCrimes();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_DATE) {
				Date crimeDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
				crime.setDate(crimeDate);
				updateDate();
			} else if (requestCode == REQUEST_PHOTO) {
				String fileName = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
				if (fileName != null) {
					Photo photo = new Photo(fileName);
					crime.setPhoto(photo);
					showPhoto();
				}
			} else if (requestCode == REQUEST_CONTACT) {
				Uri contactUri = data.getData();
				String[] queryFields = new String[] {
						ContactsContract.Contacts.DISPLAY_NAME,
						ContactsContract.Contacts._ID };
				Cursor cursor = getActivity().getContentResolver()
						.query(contactUri, queryFields, null, null, null);

				if (cursor.getCount() != 0) {
					cursor.moveToFirst();
					String suspect = cursor.getString(0);
					crime.setSuspect(suspect);
					suspectButton.setText(suspect);
				}

				cursor.close();

				Cursor cursorID = getActivity().getContentResolver().query(contactUri,
						new String[] { ContactsContract.Contacts._ID }, null, null, null);
				String contactID = null;
				if (cursorID.moveToFirst()) {

					contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
				}

				cursorID.close();

				// Using the contact ID now we will get contact phone number
				Cursor cursorPhone = getActivity().getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },

						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
								+ ContactsContract.CommonDataKinds.Phone.TYPE + " = "
								+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

						new String[] { contactID }, null);

				if (cursorPhone.moveToFirst()) {
					crime.setSuspectPhoneNumber(cursorPhone.getString(cursorPhone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				} else {
					crime.setSuspectPhoneNumber(null);
				}
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(photoView);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean optionsItemSelected;
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			optionsItemSelected = true;
			break;

		default:
			optionsItemSelected = super.onOptionsItemSelected(item);
			break;
		}
		return optionsItemSelected;
	}

	private void updateDate() {
		dateButton.setText(crime.getDate().toString());
	}

	private String getCrimeReport() {
		String solvedString = null;
		if (crime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);

		}

		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, crime.getDate()).toString();

		String suspect = crime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}

		String titleString = crime.getTitle();

		String report = getString(R.string.crime_report, titleString, dateString, solvedString, suspect);
		return report;
	}

}
