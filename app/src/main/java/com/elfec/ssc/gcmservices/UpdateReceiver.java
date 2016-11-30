package com.elfec.ssc.gcmservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class UpdateReceiver extends WakefulBroadcastReceiver { 
	
	 @Override 
	 public void onReceive(Context context, Intent intent) { 
		 ComponentName comp = new ComponentName(context.getPackageName(),
				 UpdateGCMTokenService.class.getName());
		 startWakefulService(context, (intent.setComponent(comp))); 
	 } 
}
