package com.elfec.ssc.model.gcmservices;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.util.Log;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.model.exceptions.GcmConnectException;
import com.elfec.ssc.security.AppPreferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase cuyo objetivo es obtener el GCM token del dispositivo
 *
 * @author Diego
 */
public class GcmTokenRequester {

    private Context mContext;
    private GcmTokenCallback mCallback;
    private String mCurrentTokenOnPreferences;
    private AppPreferences mPreferences;
    private List<Exception> mExceptions;
    private static final String PROJECT_NUMBER = "302707079727";

    public GcmTokenRequester(Context context) {
        mContext = context;
        mPreferences = AppPreferences.instance();
        mCurrentTokenOnPreferences = mPreferences.getGCMToken();
        mExceptions = new ArrayList<>();
    }

    /**
     * Obtiene el token del dispositivo de forma asincrona, en caso de que el token ya se
     * tuviera guardado en los shared mPreferences directamente
     * se llama al <b>callback</b>, cuando se obtiene el token se lo guarda automáticamente en
     * las shared mPreferences
     *
     * @param callback callback de obtención de token
     */
    public void getTokenAsync(GcmTokenCallback callback) {
        this.mCallback = callback;
        if (mCurrentTokenOnPreferences != null)
            mCallback.onGcmTokenReceived(mCurrentTokenOnPreferences);
        else {
            new GcmTokenRemoteRequester().execute((Void[]) null);
        }
    }

    private class GcmTokenRemoteRequester extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String deviceToken = null;
            mExceptions.clear();
            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mContext);
                deviceToken = gcm.register(PROJECT_NUMBER);

            } catch (IOException ex) {
                mExceptions.add(new GcmConnectException());
                Log.d("GCM Token Request", ex.getMessage());
            }
            return deviceToken;
        }

        @Override
        protected void onPostExecute(String deviceToken) {
            if (deviceToken != null && !deviceToken.isEmpty()) {
                mPreferences.setGCMToken(deviceToken);
                mCallback.onGcmTokenReceived(deviceToken);
            }
            else mCallback.onGcmErrors(mExceptions);
        }

    }

}
