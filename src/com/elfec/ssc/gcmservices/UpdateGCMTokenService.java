package com.elfec.ssc.gcmservices;

import com.elfec.ssc.businesslogic.webservices.DeviceWS;
import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.events.GCMTokenReceivedCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.WSTokenReceivedCallback;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.security.PreferencesManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class UpdateGCMTokenService extends IntentService{

	public UpdateGCMTokenService() {
		super("UpdateGCMTokenService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final PreferencesManager preferences = new PreferencesManager(this);
		final String lastToken = preferences.getGCMToken();
		final String IMEI = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		preferences.setGCMToken(null);
		preferences.setWSToken(null);
		new WSTokenRequester(this).getTokenAsync(new WSTokenReceivedCallback() {		
			@Override
			public void onWSTokenReceived(final WSResponse<WSToken> wsTokenResult) {
				new GCMTokenRequester(UpdateGCMTokenService.this).getTokenAsync(new GCMTokenReceivedCallback() {			
					@Override
					public void onGCMTokenReceived(String deviceToken) {
						if(deviceToken!=null && !deviceToken.isEmpty())
						{
							new DeviceWS(wsTokenResult.getResult()).updateDeviceGCMToken(lastToken, IMEI, deviceToken, new IWSFinishEvent<Boolean>() {					
								@Override
								public void executeOnFinished(WSResponse<Boolean> result) {
									if(result.hasErrors())
									{
										preferences.setGCMToken(lastToken);
										preferences.setHasToUpdateGCMToken(true);
									}
									else preferences.setHasToUpdateGCMToken(false);
								}
							});
						}
						else 
						{
							preferences.setGCMToken(lastToken);
							preferences.setHasToUpdateGCMToken(true);
						}
					}
				});
			}
		});
	}

}
