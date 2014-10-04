package com.example.criminalintent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class CriminalIntentJSONSerializer {

	private static final String TAG = "CriminalIntentJSONSerializer";

	private Context context;
	private String fileName;

	public CriminalIntentJSONSerializer(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}

	public ArrayList<Crime> loadCrimes() throws JSONException, IOException {
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			InputStream inputStream = this.context.openFileInput(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);
			StringBuilder jsonStringBuilder = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				jsonStringBuilder.append(line);
			}

			JSONArray jsonArray = (JSONArray) new JSONTokener(jsonStringBuilder.toString()).nextValue();
			for (int i = 0; i < jsonArray.length(); i++) {
				crimes.add(new Crime(jsonArray.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			Log.d(TAG, "Could not find file: " + fileName);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return crimes;
	}

	public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
		JSONArray jsonArray = new JSONArray();
		for (Crime crime : crimes) {
			jsonArray.put(crime.toJSON());
		}
		Writer writer = null;
		try {
			OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(outputStream);
			writer.write(jsonArray.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
