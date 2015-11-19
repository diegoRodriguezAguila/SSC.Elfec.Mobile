package com.elfec.ssc.security;

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
 * Maneja las sharedpreferences de toda la aplicaci�n
 * @author Diego
 *
 */
public class AppPreferences {

	private final String FIRST_APP_USAGE = "FirstAppUsage";
	private final String HAS_ONE_GMAIL_ACCOUNT = "HasAtLeastOneGmailAccount";
	private final String FIRST_LOAD_LOCATIONS="FirstLoadLocations";
	private final String SELECTED_LOCATION_POINT_TYPE = "SelectedLocationPointType";
	private final String SELECTED_LOCATION_POINT_DISTANCE = "SelectedLocationPointDistance";
	private final String SETUP_DISTANCE = "SetupDistance";
	private final String GCM_TOKEN = "GCMToken";
	private final String HAS_TO_UPDATE_GCM_TOKEN = "HasToUpdateGCMToken";
	private final String WS_TOKEN = "SscToken";
    
	/**
	 * Contexto
	 */
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
	 * Verifica si es que es la primera vez que se utiliza la aplicaci�n
	 * @return true si es que es la primera vez
	 */
	public boolean isFirstAppUsage()
	{
		return preferences.getBoolean(FIRST_APP_USAGE, true);
	}
	
	/**
	 * Asigna que la aplicación ya se ha utilizado por lo menos una vez
	 * @return la instancia actual de AppPreferences
	 */
	public AppPreferences setAppAlreadyUsed()
	{
		preferences.edit().putBoolean(FIRST_APP_USAGE, false).apply();
		return this;
	}
	/**
	 * Verifica si es que es la primera vez que se descarga las ubicaciones
	 * @return true si es que es la primera vez
	 */
	public boolean isFirstLoadLocations()
	{
		return preferences.getBoolean(FIRST_LOAD_LOCATIONS, true);
	}
	/**
	 * Asigna que la aplicación ya descargo las localizaciones
	 * @return la instancia actual de AppPreferences
	 */
	public AppPreferences setLoadLocationsAlreadyUsed()
	{
		preferences.edit().putBoolean(FIRST_LOAD_LOCATIONS, false).apply();
		return this;
	}

	/**
	 * Verifica si es que el cliente ha registrado al menos una cuenta de gmail
	 * @return true si es que si registró al menos una cuenta
	 */
	public boolean hasOneGmailAccount()
	{
		return preferences.getBoolean(HAS_ONE_GMAIL_ACCOUNT, false);
	}
	
	/**
	 * Asigna que el cliente ha registrado por lo menos una cuenta de gmail
	 * @return la instancia actual de AppPreferences
	 */
	public AppPreferences setHasOneGmailAccount()
	{
		preferences.edit().putBoolean(HAS_ONE_GMAIL_ACCOUNT, true).apply();
		return this;
	}
	
	/**
	 * Obtiene el tipo de punto de ubicación que fué seleccionado en los radio buttons de
     * locationservices
	 * @return
	 */
	public LocationPointType getSelectedLocationPointType()
	{
		return LocationPointType.get(Short.parseShort(preferences.getString(
                SELECTED_LOCATION_POINT_TYPE, ""+LocationPointType.BOTH.toShort())));
	}
	
	/**
	 * Guarda el tipo de punto de ubicación que fué seleccionado en los radio buttons de
     * locationservices
	 * @param type
	 */
	public void setSelectedLocationPointType(LocationPointType type)
	{
		preferences.edit().putString(SELECTED_LOCATION_POINT_TYPE, ""+type.toShort()).commit();
	}
	
	/**
	 * Obtiene el tipo de distancia de los puntos de ubicación, que fué seleccionado en los radio
     * buttons de locationservices
	 * @return
	 */
	public LocationDistance getSelectedLocationPointDistance()
	{
		return LocationDistance.get(Short.parseShort(preferences.getString(
                SELECTED_LOCATION_POINT_DISTANCE, ""+LocationDistance.ALL.toShort())));
	}
	
	/**
	 * Guarda el tipo de distancia de los puntos de ubicación, que fué seleccionado en los radio
     * buttons de locationservices
	 * @param distance
	 */
	public void setSelectedLocationPointDistance(LocationDistance distance)
	{
		preferences.edit().putString(SELECTED_LOCATION_POINT_DISTANCE, ""+distance.toShort()).commit();
	}

	/**
	 * Obtiene la distancia configurada por el cliente para los servicios de ubicación
	 * @return
	 */
	public int getConfiguredDistance()
	{
		return preferences.getInt(SETUP_DISTANCE, 1000);
	}

	/**
	 * Guarda la distancia configurada por el cliente para los servicios de ubicación
	 * @param distance distancia
	 */
	public void setConfiguredDistance(int distance)
	{
		preferences.edit().putInt(SETUP_DISTANCE, distance).apply();
	}
	
	/**
	 * Obtiene el GCM token del dispositivo
	 * @return
	 */
	public String getGCMToken()
	{
		return preferences.getString(GCM_TOKEN, null);
	}
	
	/**
	 * Guarda el GCM token del dispositivo
	 * @param gcmToken gcm token
	 */
	public void setGCMToken(String gcmToken)
	{
		preferences.edit().putString(GCM_TOKEN, gcmToken).apply();
	}
	
	/**
	 * Verifica si es que se tiene que realizar el envío del token al servidor
	 * @return true si es que si 
	 */
	public boolean hasToUpdateGCMToken()
	{
		return preferences.getBoolean(HAS_TO_UPDATE_GCM_TOKEN, false);
	}
	
	/**
	 * Asigna que se tiene que realizar el envío del token al servidor
	 * @return la instancia actual de AppPreferences
	 */
	public AppPreferences setHasToUpdateGCMToken(boolean hasToUpdateIt)
	{
		preferences.edit().putBoolean(HAS_TO_UPDATE_GCM_TOKEN, hasToUpdateIt).apply();
		return this;
	}
	
	/**
	 * Guarda el sscToken
	 * @param sscToken ssc token
	 */
	public void setWSToken(SscToken sscToken)
	{
		preferences.edit().putString(WS_TOKEN, sscToken !=null? sscToken.toString():null).commit();
	}
	
	/**
	 * Obtiene el wsToken
	 * @return wsToken
	 */
	public SscToken getWSToken()
	{
		try {
			JSONObject json = new JSONObject(preferences.getString(WS_TOKEN, null));
			return new SscToken(json.getString("imei"), json.getString("token"));
		} 
		catch (JSONException | NullPointerException ignored) {}
        return null;
	}
}
