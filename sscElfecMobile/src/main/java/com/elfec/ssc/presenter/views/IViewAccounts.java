package com.elfec.ssc.presenter.views;

import java.util.List;

import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.security.PreferencesManager;

public interface IViewAccounts {
	/**
	 * Muestra una lista de cuentas, si se pasa null o una lista vacia
	 * se muestra un mensaje por defecto al usuario de que no hay cuentas
	 * @param result
	 */
	public void showAccounts(List<Account> result);
	/**
	 * Obtiene el PreferencesManager con el contexto de la aplicaci�n global
	 * @return {@link PreferencesManager}
	 */
	public PreferencesManager getPreferences();
	/**
	 * Obtiene el IMEI del dispositivo
	 * @return
	 */
	public String getIMEI();
	/**
	 * Refresca la lista de cuentas
	 */
	public void refreshAccounts();
	/**
	 * Muestra errores al usuario
	 * @param errors
	 */
	public void displayErrors(List<Exception> errors);
	public void dialogRemove(int position); 
	/**
	 * Muestra un dialogo de espera
	 */
	public void showWSWaiting();
	/**
	 * Oculta el dialogo de espera
	 */
	public void hideWSWaiting();
	/**
	 * Indica si es que la lista se refresc� recientemente
	 * @return
	 */
	public boolean isRefreshed();
	/**
	 * Muestra errores que podr�an ocurrir al obtener las cuentas
	 * @param errors
	 */
	public void showViewAccountsErrors(List<Exception> errors);
	/**
	 * Obtiene el GCMTokenRequester con el contexto de la actividad actual
	 * @return
	 */
	public GCMTokenRequester getGCMTokenRequester();
	/**
	 * Obtiene el WSTokenRequester con el contexto de la actividad actual
	 * @return
	 */
	public WSTokenRequester getWSTokenRequester();
}
