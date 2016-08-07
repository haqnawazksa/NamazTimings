package com.example.namaztimings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class NamazAlarmReceiver extends BroadcastReceiver implements Runnable {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// MediaPlayer mp = MediaPlayer.create(context, R.raw.adhan);
		// mp.start();
		context.startService(new Intent(context, AdhanService.class));
		Thread t = new Thread(this);
		t.start();
	}

	public void cancelAlarm() {
		for (int i = 0; i < list.size(); i++) {

			am.cancel(list.get(i));

		}
		list.clear();

	}

	AlarmManager am;
	List<PendingIntent> list = new ArrayList();

	public void setNamazAlarm(Context con, String s) throws Exception {
		am = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
		String[] arr = s.split(" ");
		Intent intent = new Intent(con, NamazAlarmReceiver.class);
		intent.putExtra("onetime", Boolean.FALSE);
		intent.putExtra(arr[0], s);
		PendingIntent pi = PendingIntent.getBroadcast(con, 0, intent, 0);
		// c1.setTimeInMillis(System.currentTimeMillis());
		Calendar c = Calendar.getInstance();
		// c.setTimeInMillis(System.currentTimeMillis());
		String[] hourMinutes = arr[1].split(":");
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinutes[0]));

		c.set(Calendar.MINUTE, Integer.parseInt(hourMinutes[1]));
		c.set(Calendar.SECOND, 0);
		// After after 5 seconds
		if (c.getTimeInMillis() > System.currentTimeMillis()) {
			am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
			list.add(pi);
		}
	}

	@Override
	public void run() {
		long timeToGo = System.currentTimeMillis() + 15 *60* 1000;
		while (System.currentTimeMillis() < timeToGo) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		MainActivity.ma.unregisterReceiver(MainActivity.sms);
		MainActivity.ma.unregisterReceiver(MainActivity.call);
		final AudioManager rm = (AudioManager) MainActivity.ma
				.getSystemService(Context.AUDIO_SERVICE);
		rm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

	}
}
