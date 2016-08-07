package com.example.namaztimings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences sp = getSharedPreferences("isServiceOn", MODE_PRIVATE);
		Editor e = sp.edit();
		e.putBoolean("buttonState", tb.isChecked());
		e.commit();

	}

	public static MainActivity ma;
	public static SMSReceiver sms;
	public static CallReceiver call;
	ToggleButton tb;

	public void toggleService(View v) {
		tb = (ToggleButton) v;

		if (tb.isChecked()) {
			startService(new Intent(this, UpdateService.class));
		} else {
			stopService(new Intent(this, AdhanService.class));

			stopService(new Intent(this, UpdateService.class));
		}

	}

	public static String[] arr = new String[5];

	private class NamazTimingsTask extends AsyncTask<URL, Integer, Long> {

		@Override
		protected Long doInBackground(URL... arg0) {
			// TODO Auto-generated method stub
			try {
				URL url = arg0[0];
				yc = url.openConnection();
				InputStreamReader reader = new InputStreamReader(
						yc.getInputStream());
				in = new BufferedReader(reader);
				String inputLine = "";
				content = "";
				while ((inputLine = in.readLine()) != null)
					content += inputLine;
				mutex = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	String content = "";
	BufferedReader in;
	URLConnection yc;
	boolean mutex = true;
	ListView lv;
	final ArrayList<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ma = this;
		tb = (ToggleButton) findViewById(R.id.toggleButton1);
		SharedPreferences sp = getSharedPreferences("isServiceOn", MODE_PRIVATE);
		tb.setChecked(sp.getBoolean("buttonState", false));
		if (tb.isChecked()) {
			startService(new Intent(this, UpdateService.class));

		}

		lv = (ListView) findViewById(R.id.data);
		JSONObject json = null;
		Calendar c = Calendar.getInstance();
		String s = "http://api.xhanch.com/islamic-get-prayer-time.php?lng=67.0100&lat=24.8600&gmt=5&m=json&yy=2016&mm=4"
				+ (c.get(Calendar.MONTH) + 1);

		// final ArrayList<String> list = new ArrayList<String>();
		try {
			new NamazTimingsTask().execute(new URL(s));

			while (mutex) {
				Thread.sleep(1000);
			}

			in.close();

			json = new org.json.JSONObject(content);

			list.add("Fajr "
					+ json.getJSONObject(
							String.valueOf(c.get(Calendar.DAY_OF_MONTH)))
							.getString("fajr"));
			arr[0] = list.get(list.size() - 1);
			list.add("Dhuhr "
					+ json.getJSONObject(
							String.valueOf(c.get(Calendar.DAY_OF_MONTH)))
							.getString("zuhr"));
			arr[1] = list.get(list.size() - 1);
			list.add("Asr "
					+ json.getJSONObject(
							String.valueOf(c.get(Calendar.DAY_OF_MONTH)))
							.getString("asr"));
			arr[2] = list.get(list.size() - 1);
			list.add("Maghrib "
					+ json.getJSONObject(
							String.valueOf(c.get(Calendar.DAY_OF_MONTH)))
							.getString("maghrib"));
			arr[3] = list.get(list.size() - 1);
			list.add("Isha " + "20:54");
			arr[4] = list.get(list.size() - 1);
		} catch (Exception ex) {
			Log.d("MYTAG", ex.getMessage());

		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		// lv.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// NamazAlarmReceiver nar = new NamazAlarmReceiver();
		// try {
		// String s = list.get(arg2);
		// nar.setNamazAlarm(MainActivity.this, s);
		// } catch (Exception ex) {
		// Log.d("MYTAG", ex.getMessage());
		// }
		// }
		// });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
