package com.elfec.ssc.model.gcmservices;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.util.Log;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.security.AppPreferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Clase cuyo objetivo es obtener el GCM token del dispositivo
 *
 * @author Diego
 */
public class GCMTokenRequester extends AsyncTask<Void, Void, String> {

    private Context mContext;
    private GcmTokenCallback mCallback;
    private String currentTokenOnPreferences;
    private AppPreferences preferences;
    private static final String PROJECT_NUMBER = "302707079727";

    public GCMTokenRequester(Context context) {
        this.mContext = context;
        this.preferences = AppPreferences.instance();
        this.currentTokenOnPreferences = preferences.getGCMToken();
    }

    @Override
    protected String doInBackground(Void... params) {
        String deviceToken = null;
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);
            deviceToken = gcm.register(PROJECT_NUMBER);

        } catch (IOException ex) {
            Log.e("GCM Token Request", ex.getMessage());
        }
        return deviceToken;
    }

    @Override
    protected void onPostExecute(String deviceToken) {
        if (deviceToken != null && !deviceToken.isEmpty())
            preferences.setGCMToken(deviceToken);
        if (mCallback != null)
            mCallback.onGcmTokenReceived(deviceToken);
    }

    /**
     * Obtiene el token del dispositivo de forma asincrona, en caso de que el token ya se
     * tuviera guardado en los shared preferences directamente
     * se llama al <b>mCallback</b>, cuando se obtiene el token se lo guarda automáticamente en
     * las shared preferences
     *
     * @param callback mCallback de obtención de token
     */
    public void getTokenAsync(GcmTokenCallback callback) {
        this.mCallback = callback;
        if (currentTokenOnPreferences != null)
            callback.onGcmTokenReceived(currentTokenOnPreferences);
        else {
            this.execute((Void[]) null);
        }
    }

}
