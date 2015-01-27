package com.elfec.ssc.model.gcmservices;

import java.io.IOException;

import com.activeandroid.util.Log;
import com.elfec.ssc.model.events.GCMTokenReceivedCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;

public class GCMTokenRequester extends AsyncTask<Void, Void, String> {

	private Context context;
	private GCMTokenReceivedCallback callback;
	private final String PROJECT_NUMBER="302707079727";
	
	public GCMTokenRequester(Context context) {
		this.context = context;
	}
	
	public GCMTokenRequester(Context context, GCMTokenReceivedCallback callback) {
		this.context = context;
		this.callback = callback;
	}

	public void setCallback(GCMTokenReceivedCallback callback) {
		this.callback = callback;
	}

	@Override
	protected String doInBackground(Void... params) {
		String deviceToken=null;
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            deviceToken = gcm.register(PROJECT_NUMBER);
	           
        } 
        catch (IOException ex) {
        	Log.d("GCM Token Request", ex.getMessage());
        }
        return deviceToken;
	}
	
	 @Override
     protected void onPostExecute(String deviceToken) {
		 if(callback!=null)
			 callback.onGCMTokenReceived(deviceToken);
     }

}
