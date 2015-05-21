package com.elfec.ssc.model.gcmservices;

import java.io.IOException;

import com.activeandroid.util.Log;
import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.model.events.GCMTokenReceivedCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Clase cuyo objetivo es obtener el GCM token del dispositivo
 * @author Diego
 *
 */
public class GCMTokenRequester extends AsyncTask<Void, Void, String> {

	private Context context;
	private GCMTokenReceivedCallback callback;
	private String currentTokenOnPreferences;
	private PreferencesManager preferences;
	private final String PROJECT_NUMBER="302707079727";
	
	public GCMTokenRequester(Context context) {
		this.context = context;
		this.preferences = new PreferencesManager(context);
		this.currentTokenOnPreferences = preferences.getGCMToken();
	}

	@Override
	protected String doInBackground(Void... params) {
		String deviceToken=null;
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            deviceToken = gcm.register(PROJECT_NUMBER);
	           
        } 
        catch (IOException ex) {
        	Log.e("GCM Token Request", ex.getMessage());
        }
        return deviceToken;
	}
	
	 @Override
     protected void onPostExecute(String deviceToken) {
		 if(deviceToken!=null && !deviceToken.isEmpty())
			 preferences.setGCMToken(deviceToken);
		 if(callback!=null)
			 callback.onGCMTokenReceived(deviceToken);
     }
	 
	 /**
	  * Obtiene el token del dispositivo de forma asincrona, en caso de que el token ya se
	  * tuviera guardado en los shared preferences directamente
	  * se llama al <b>callback</b>, cuando se obtiene el token se lo guarda automáticamente en 
	  * las shared preferences
	  * @param callback
	  */
	 public void getTokenAsync(GCMTokenReceivedCallback callback)
	 {
		 this.callback = callback;
		 if(currentTokenOnPreferences!=null)
			 callback.onGCMTokenReceived(currentTokenOnPreferences);
		 else
		 {
			 this.execute((Void[])null);
		 }
	 }

}
