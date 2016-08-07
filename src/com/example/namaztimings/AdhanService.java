package com.example.namaztimings;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

public class AdhanService extends Service {

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.stop();
	}

	MediaPlayer mp;

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		final AudioManager rm = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		rm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		mp = MediaPlayer.create(getApplicationContext(), R.raw.adhan);
		mp.start();
		SMSReceiver sms = new SMSReceiver();
		CallReceiver call = new CallReceiver();
		IntentFilter i = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		IntentFilter i2 = new IntentFilter("android.intent.action.PHONE_STATE");
		registerReceiver(sms, i);
		registerReceiver(call, i2);
		MainActivity.sms = sms;
		MainActivity.call = call;

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
