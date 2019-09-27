package com.example.hees.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class VoiceMessageCall extends BroadcastReceiver {

    SmsManager smsManager = SmsManager.getDefault();


    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
