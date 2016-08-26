package abbyy.ocrsdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Translator extends AsyncTask<String, Void, String> {

	static final String API_KEY = "AIzaSyB82Q-nTI80_F70k3D9axlEfE1Z0F8cbC4";

	static final String API_URL = "https://www.googleapis.com/language/translate/v2?";

	ResultsActivity r;
	String ip, ip2, iip;

	public Translator(Context c) {
		r = (ResultsActivity) c;
	}

	protected String doInBackground(String... urls) {
		// Do some validation here
		
		{
		iip = urls[0];
		ip = urls[0].replaceAll("\\s+", "+");
		}
		ip2 = urls[1];
		try {
			URL url = new URL(API_URL + "key=" + API_KEY + "&source=en&target="
					+ ip2 + "&q=" + ip);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
				bufferedReader.close();
				return stringBuilder.toString();
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage(), e);
			return null;
		}
	}

	protected void onPostExecute(String response) {
		if (response == null) {
			response = "THERE WAS AN ERROR";
		}
		// JSONObject jObject = new JSONObject(response);
		// String aJsonString = jObject.getString("translatedText");
		// progressBar.setVisibility(View.GONE);

		// TODO: check this.exception
		// TODO: do something with the feed
		String data = "";
		JSONArray contacts = null;
		JSONObject contact;

		try {
			JSONObject jsonRootObject = new JSONObject(response);

			contact = jsonRootObject.getJSONObject("data");
			contacts = contact.getJSONArray("translations");
			for (int i = 0; i < contacts.length(); i++) {
				JSONObject c = contacts.getJSONObject(i);

				String name = c.getString("translatedText");
				data = name;

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		Log.i("INFO", data);
		 r.displayMessage("INPUT:\n"+iip+"\n\nOUTPUT:\n"+ data);

	}

}