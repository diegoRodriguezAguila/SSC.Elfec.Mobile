package com.elfec.ssc.helpers;

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
}
