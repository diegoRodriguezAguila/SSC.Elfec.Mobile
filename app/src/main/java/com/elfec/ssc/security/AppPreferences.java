package com.elfec.ssc.security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.model.exceptions.InitializationException;
import com.elfec.ssc.model.security.SscToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;

/**
 * Maneja las sharedpreferences de toda la aplicación
 *
 * @author Diego
 */
public class AppPreferences {

    private static final String FIRST_APP_USAGE = "FirstAppUsage";
    private static final String HAS_ONE_GMAIL_ACCOUNT = "HasAtLeastOneGmailAccount";
    private static final String SELECTED_LOCATION_POINT_TYPE = "SelectedLocationPointType";
    private static final String SELECTED_LOCATION_POINT_DISTANCE = "SelectedLocationPointDistance";
    private static final String SETUP_DISTANCE = "SetupDistance";
    private static final String SSC_TOKEN = "SscToken";
    private static final String HAS_TO_SEND_GCM_TOKEN = "HasToSendGcmToken";

    /**
     * Contexto
     */
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    /**
     * Referencia débil de la instancia de appPreferences
     */
    private static SoftReference<AppPreferences> appPreferencesInstanceRef;

    private SharedPreferences preferences;

    private AppPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * Este método se debe llamar al inicializar la aplicación
     *
     * @param context sContext
     */
    public static void init(Context context) {
        AppPreferences.sContext = context;
    }

    /**
     * Obtiene el contexto de la aplicación
     *
     * @return el contexto de la aplicación
     */
    public static Context getApplicationContext() {
        return AppPreferences.sContext;
    }

    public static AppPreferences instance() {
        if (appPreferencesInstanceRef == null || appPreferencesInstanceRef.get() == null) {
            if (sContext == null)
                throw new InitializationException(AppPreferences.class);
            appPreferencesInstanceRef = new SoftReference<>(new AppPreferences(sContext));
        }
        return appPreferencesInstanceRef.get();
    }


    /**
     * Verifica si es que es la primera vez que se utiliza la aplicación
     *
     * @return true si es que es la primera vez
     */
    public boolean isFirstAppUsage() {
        return preferences.getBoolean(FIRST_APP_USAGE, true);
    }

    /**
     * Asigna que la aplicación ya se ha utilizado por lo menos una vez
     *
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setAppAlreadyUsed() {
        preferences.edit().putBoolean(FIRST_APP_USAGE, false).apply();
        return this;
    }

    /**
     * Verifica si es que el cliente ha registrado al menos una cuenta de gmail
     *
     * @return true si es que si registró al menos una cuenta
     */
    public boolean hasOneGmailAccount() {
        return preferences.getBoolean(HAS_ONE_GMAIL_ACCOUNT, false);
    }

    /**
     * Asigna que el cliente ha registrado por lo menos una cuenta de gmail
     *
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setHasOneGmailAccount() {
        preferences.edit().putBoolean(HAS_ONE_GMAIL_ACCOUNT, true).apply();
        return this;
    }

    /**
     * Obtiene el tipo de punto de ubicación que fué seleccionado en los radio buttons de
     * locationservices
     *
     * @return LocationPointType
     */
    public LocationPointType getSelectedLocationPointType() {
        return LocationPointType.get(Short.parseShort(preferences.getString(
                SELECTED_LOCATION_POINT_TYPE, "" + LocationPointType.BOTH.toShort())));
    }

    /**
     * Guarda el tipo de punto de ubicación que fué seleccionado en los radio buttons de
     * locationservices
     *
     * @param type LocationPointType
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setSelectedLocationPointType(LocationPointType type) {
        preferences.edit().putString(SELECTED_LOCATION_POINT_TYPE, "" + type.toShort()).apply();
        return this;
    }

    /**
     * Obtiene el tipo de distancia de los puntos de ubicación, que fué seleccionado en los radio
     * buttons de locationservices
     *
     * @return LocationDistance
     */
    public LocationDistance getSelectedLocationPointDistance() {
        return LocationDistance.get(Short.parseShort(preferences.getString(
                SELECTED_LOCATION_POINT_DISTANCE, "" + LocationDistance.ALL.toShort())));
    }

    /**
     * Guarda el tipo de distancia de los puntos de ubicación, que fué seleccionado en los radio
     * buttons de locationservices
     *
     * @param distance LocationDistance
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setSelectedLocationPointDistance(LocationDistance distance) {
        preferences.edit().putString(SELECTED_LOCATION_POINT_DISTANCE,
                String.valueOf(distance.toShort())).apply();
        return this;
    }

    /**
     * Obtiene la distancia configurada por el cliente para los servicios de ubicación
     *
     * @return distance
     */
    public int getConfiguredDistance() {
        return preferences.getInt(SETUP_DISTANCE, 1000);
    }

    /**
     * Guarda la distancia configurada por el cliente para los servicios de ubicación
     *
     * @param distance distancia
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setConfiguredDistance(int distance) {
        preferences.edit().putInt(SETUP_DISTANCE, distance).apply();
        return this;
    }

    /**
     * Obtiene el SscToken
     *
     * @return SscToken
     */
    public SscToken getSscToken() {
        try {
            JSONObject json = new JSONObject(preferences.getString(SSC_TOKEN, null));
            return new SscToken(json.getString("imei"), json.getString("token"));
        } catch (JSONException | NullPointerException ignored) {
        }
        return null;
    }

    /**
     * Guarda el sscToken
     *
     * @param sscToken ssc token
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setSscToken(SscToken sscToken) {
        preferences.edit().putString(SSC_TOKEN, sscToken != null ? sscToken.toString() : null)
                .apply();
        return this;
    }

    /**
     * Verifies if the gcm token should be sent to the server
     *
     * @return true if it has to
     */
    public boolean hasToSendGcmToken() {
        return preferences.getBoolean(HAS_TO_SEND_GCM_TOKEN, true);
    }

    /**
     * Sets the preferences pointing out the gcm token should be sent to the server
     *
     * @param hasToSend true or false
     * @return this instance of {@link AppPreferences}
     */
    public AppPreferences setHasToSendGcmToken(boolean hasToSend) {
        preferences.edit().putBoolean(HAS_TO_SEND_GCM_TOKEN, hasToSend).apply();
        return this;
    }
}
