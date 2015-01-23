package com.elfec.ssc.helpers;

import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Maneja las sharedpreferences de toda la aplicación
 * @author Diego
 *
 */
public class PreferencesManager {

	private final String FIRST_APP_USAGE = "FirstAppUsage";
	private final String HAS_ONE_GMAIL_ACCOUNT = "HasAtLeastOneGmailAccount";
	private final String FIRST_LOAD_ACCOUNTS = "FirstLoadAccounts";
	private final String FIRST_LOAD_LOCATIONS="FirstLoadLocations";
	private final String SELECTED_LOCATION_POINT_TYPE = "SelectedLocationPointType";
	private final String SELECTED_LOCATION_POINT_DISTANCE = "SelectedLocationPointDistance";
	private final String SETUP_DISTANCE = "SetupDistance";
	
	private SharedPreferences preferences;
	
	public PreferencesManager(Context context)
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	/**
	 * Verifica si es que es la primera vez que se utiliza la aplicación
	 * @return true si es que es la primera vez
	 */
	public boolean isFirstAppUsage()
	{
		return preferences.getBoolean(FIRST_APP_USAGE, true);
	}
	
	/**
	 * Asigna que la aplicación ya se ha utilizado por lo menos una vez
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setAppAlreadyUsed()
	{
		preferences.edit().putBoolean(FIRST_APP_USAGE, false).commit();
		return this;
	}
	/**
	 * Verifica si es que es la primera vez que se descarga las cuentas
	 * @return true si es que es la primera vez
	 */
	public boolean isFirstLoadAccounts()
	{
		return preferences.getBoolean(FIRST_LOAD_ACCOUNTS, true);
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
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setLoadLocationsAlreadyUsed()
	{
		preferences.edit().putBoolean(FIRST_LOAD_LOCATIONS, false).commit();
		return this;
	}
	/**
	 * Asigna que la aplicación ya descargo las cuentas para un usuario
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setLoadAccountsAlreadyUsed()
	{
		preferences.edit().putBoolean(FIRST_LOAD_ACCOUNTS, false).commit();
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
	 * @return la instancia actual de PreferencesManager
	 */
	public PreferencesManager setHasOneGmailAccount()
	{
		preferences.edit().putBoolean(HAS_ONE_GMAIL_ACCOUNT, true).commit();
		return this;
	}
	
	/**
	 * Obtiene el tipo de punto de ubicación que fué seleccionado en los radio buttons de locationservices
	 * @return
	 */
	public LocationPointType getSelectedLocationPointType()
	{
		return LocationPointType.get(Short.parseShort(preferences.getString(SELECTED_LOCATION_POINT_TYPE, ""+LocationPointType.BOTH.toShort())));
	}
	
	/**
	 * Guarda el tipo de punto de ubicación que fué seleccionado en los radio buttons de locationservices
	 * @param type
	 */
	public void setSelectedLocationPointType(LocationPointType type)
	{
		preferences.edit().putString(SELECTED_LOCATION_POINT_TYPE, ""+type.toShort()).commit();
	}
	
	/**
	 * Obtiene el tipo de distancia de los puntos de ubicación, que fué seleccionado en los radio buttons de locationservices
	 * @return
	 */
	public LocationDistance getSelectedLocationPointDistance()
	{
		return LocationDistance.get(Short.parseShort(preferences.getString(SELECTED_LOCATION_POINT_DISTANCE, ""+LocationDistance.ALL.toShort())));
	}
	
	/**
	 * Guarda el tipo de distancia de los puntos de ubicación, que fué seleccionado en los radio buttons de locationservices
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
	 * @param type
	 */
	public void setConfiguredDistance(int distance)
	{
		preferences.edit().putInt(SETUP_DISTANCE, distance).commit();
	}
}
