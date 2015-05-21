package com.elfec.ssc.gcmservices;

import com.elfec.ssc.helpers.PreferencesManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.WakefulBroadcastReceiver;


public class InternetEnabledReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(new PreferencesManager(context).hasToUpdateGCMToken() && isNetworkAvaible(context))
		{
			 ComponentName comp = new ComponentName(context.getPackageName(),
					 UpdateGCMTokenService.class.getName());
			 startWakefulService(context, (intent.setComponent(comp))); 
		}
	}
	
	/**
	 * Verifica si la conexión a la red está activa
	 * @param context
	 * @return true si es que está conectado a una red
	 */
	public boolean isNetworkAvaible(Context context)
	{
		ConnectivityManager cm =
		        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		    return cm.getActiveNetworkInfo() != null && 
		       cm.getActiveNetworkInfo().isConnected();
	}
}
