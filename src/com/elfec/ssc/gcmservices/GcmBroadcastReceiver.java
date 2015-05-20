package com.elfec.ssc.gcmservices;
import com.elfec.ssc.helpers.PreferencesManager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmMessageService.class.getName());
        String deviceToken = intent.getExtras().getString("registration_id"); 
        
        if(deviceToken != null && !deviceToken.equals("")) { 
        	PreferencesManager preferences = new PreferencesManager(context);
   			preferences.setGCMToken(deviceToken);
        }
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
