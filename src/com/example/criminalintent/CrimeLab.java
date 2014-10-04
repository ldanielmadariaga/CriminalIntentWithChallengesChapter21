package com.example.criminalintent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

public class CrimeLab {

	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	private static CrimeLab crimeLab;

	private Context appContext;

	private ArrayList<Crime> crimes;
	private CriminalIntentJSONSerializer criminalIntentJSONSerializer;

	private CrimeLab(Context appContext) {
		this.appContext = appContext;
		criminalIntentJSONSerializer = new CriminalIntentJSONSerializer(this.appContext, FILENAME);
		try {
			crimes = criminalIntentJSONSerializer.loadCrimes();
		} catch (JSONException e) {
			logSerializationLoadError(e);
			createEmptyCrimeList();
		} catch (IOException e) {
			logSerializationLoadError(e);
			createEmptyCrimeList();
		}
	}

	public static CrimeLab getInstance(Context appContext) {
		if (crimeLab == null) {
			crimeLab = new CrimeLab(appContext.getApplicationContext());
		}
		return crimeLab;

	}

	public ArrayList<Crime> getCrimes() {
		return crimes;
	}

	public void addCrime(Crime crime) {
		crimes.add(crime);
	}

	public void deleteCrime(Crime crime) {
		crimes.remove(crime);
	}

	public Crime getCrime(UUID id) {
		Crime searchedCrime = null;
		for (Crime crime : crimes) {
			if (crime.getId().equals(id)) {
				searchedCrime = crime;
			}
		}
		return searchedCrime;
	}

	public boolean saveCrimes() {
		boolean areCrimesSaved = false;
		try {
			criminalIntentJSONSerializer.saveCrimes(crimes);
			Log.d(TAG, "Crimes saved to file");
			areCrimesSaved = true;
		} catch (JSONException e) {
			logSerializationSaveError(e);
		} catch (IOException e) {
			logSerializationSaveError(e);
		}
		return areCrimesSaved;
	}

	private void createEmptyCrimeList() {
		this.crimes = new ArrayList<Crime>();
	}

	private void logSerializationSaveError(Exception exception) {
		Log.e(TAG, "Error saving crimes: ", exception);
	}

	private void logSerializationLoadError(Exception exception) {
		Log.e(TAG, "Error loading crimes: ", exception);
	}
}
