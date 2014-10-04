package com.example.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {

	private static final String JSON_FILENAME = "filename";
	private String filename;

	public Photo(String filename) {
		this.filename = filename;
	}

	public Photo(JSONObject jsonObject) throws JSONException {
		filename = jsonObject.getString(JSON_FILENAME);
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_FILENAME, filename);
		return jsonObject;
	}

	public String getFilename() {
		return filename;
	}
}
