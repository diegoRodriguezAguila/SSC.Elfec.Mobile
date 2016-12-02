package com.elfec.ssc.messaging;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.elfec.ssc.security.AppPreferences;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmMessageService.class.getName());
        String deviceToken = intent.getExtras().getString("registration_id");   
        if(deviceToken != null && !deviceToken.equals("")) {
            AppPreferences.instance().setGcmToken(deviceToken);
        } 
        else {
	        // Start the service, keeping the device awake while it is launching.
	        startWakefulService(context, (intent.setComponent(comp)));
        }
        setResultCode(Activity.RESULT_OK);
    }
}
