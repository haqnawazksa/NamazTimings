package com.example.namaztimings;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		nar.cancelAlarm();
	}

	NamazAlarmReceiver nar;

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		nar = new NamazAlarmReceiver();
		try {
			for (int i = 0; i < 5; i++) {
				nar.setNamazAlarm(getApplicationContext(), MainActivity.arr[i]);
			}
		} catch (Exception ex) {

		}

	}

}
