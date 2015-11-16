package com.elfec.ssc.gcmservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.elfec.ssc.security.AppPreferences;


public class InternetEnabledReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(AppPreferences.instance().hasToUpdateGCMToken() && isNetworkAvaible(context))
		{
			 ComponentName comp = new ComponentName(context.getPackageName(),
					 UpdateGCMTokenService.class.getName());
			 startWakefulService(context, (intent.setComponent(comp))); 
		}
	}
	
	/**
	 * Verifica si la conexi�n a la red est� activa
	 * @param context
	 * @return true si es que est� conectado a una red
	 */
	public boolean isNetworkAvaible(Context context)
	{
		ConnectivityManager cm =
		        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		    return cm.getActiveNetworkInfo() != null && 
		       cm.getActiveNetworkInfo().isConnected();
	}
}
