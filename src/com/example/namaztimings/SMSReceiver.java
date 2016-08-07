package com.example.namaztimings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		SmsManager sms = SmsManager.getDefault();
		Object[] pdus = (Object[]) bundle.get("pdus");
		final SmsMessage[] messages = new SmsMessage[pdus.length];
		for (int i = 0; i < pdus.length; i++) {
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String s = messages[i].getOriginatingAddress();
			s = "00" + s.substring(1, s.length());
			sms.sendTextMessage(s, null, "I am praying", null, null);
		}

	}
}
