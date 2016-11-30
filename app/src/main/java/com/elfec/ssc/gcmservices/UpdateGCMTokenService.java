package com.elfec.ssc.gcmservices;

import android.app.IntentService;
import android.content.Intent;

import com.elfec.ssc.businesslogic.webservices.DeviceWS;
import com.elfec.ssc.businesslogic.webservices.SSLConection;
import com.elfec.ssc.businesslogic.webservices.SscTokenRequester;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.SscTokenReceivedCallback;
import com.elfec.ssc.model.gcmservices.GcmTokenRequester;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;

import java.util.List;

public class UpdateGCMTokenService extends IntentService {

    public UpdateGCMTokenService() {
        super("UpdateGCMTokenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SSLConection.allowSelfSignedElfecSSL(this);
        final AppPreferences preferences = AppPreferences.instance();
        final String lastToken = preferences.getGCMToken();
        //Eliminamos token ssc antiguo para que renueve con variables
        //de nueva versión de aplicación
        preferences.setSscToken(null);
        //Ejecutar solo si existe antiguo token
        if(lastToken!=null) {
            final String imei = new CredentialManager(this).getDeviceIdentifier();
            preferences.setGcmToken(null);
            new SscTokenRequester(this).getTokenAsync(new SscTokenReceivedCallback() {
                @Override
                public void onSscTokenReceived(
                        final WSResponse<SscToken> wsTokenResult) {
                    new GcmTokenRequester(UpdateGCMTokenService.this)
                            .getTokenAsync(new GcmTokenCallback() {
                                @Override
                                public void onGcmTokenReceived(String gcmToken) {
                                    new DeviceWS(wsTokenResult.getResult()).updateDeviceGCMToken(
                                            lastToken, imei, gcmToken,
                                            new IWSFinishEvent<Boolean>() {
                                                @Override
                                                public void executeOnFinished(
                                                        WSResponse<Boolean> result) {
                                                    if (result.hasErrors())
                                                        resetGcmToken(preferences, lastToken);
                                                    else preferences
                                                                .setHasToUpdateGcmToken(false);
                                                }
                                            });
                                }

                                @Override
                                public void onGcmErrors(List<Exception> errors) {
                                    resetGcmToken(preferences, lastToken);
                                }
                            });
                }
            });
        }
    }

    private void resetGcmToken(AppPreferences preferences, String lastToken) {
        preferences.setGcmToken(lastToken);
        preferences.setHasToUpdateGcmToken(true);
    }

}
