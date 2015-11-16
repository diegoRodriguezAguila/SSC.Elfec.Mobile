package com.elfec.ssc.presenter.views;

import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.security.PreferencesManager;

import java.util.List;

public interface IViewAccounts {
	/**
	 * Muestra una lista de cuentas, si se pasa null o una lista vacia
	 * se muestra un mensaje por defecto al usuario de que no hay cuentas
	 * @param result
	 */
	void showAccounts(List<Account> result);
	/**
	 * Obtiene el PreferencesManager con el contexto de la aplicación global
	 * @return {@link PreferencesManager}
	 */
	PreferencesManager getPreferences();
	/**
	 * Obtiene el IMEI del dispositivo
	 * @return
	 */
	String getIMEI();
	/**
	 * Muestra un mensaje de que la cuenta se borró exitosamente
	 */
	void showAccountDeleted();
	/**
	 * Muestra errores al usuario
	 * @param errors
	 */
	void displayErrors(List<Exception> errors);
	void dialogRemove(int position); 
	/**
	 * Muestra un dialogo de espera
	 */
	void showWSWaiting();
	/**
	 * Oculta el dialogo de espera
	 */
	void hideWSWaiting();
	/**
	 * Indica si es que la lista se refrescó recientemente
	 * @return
	 */
	boolean isRefreshed();
	/**
	 * Muestra errores que podrían ocurrir al obtener las cuentas
	 * @param errors
	 */
	void showViewAccountsErrors(List<Exception> errors);
	/**
	 * Obtiene el GCMTokenRequester con el contexto de la actividad actual
	 * @return
	 */
	GCMTokenRequester getGCMTokenRequester();
	/**
	 * Obtiene el WSTokenRequester con el contexto de la actividad actual
	 * @return
	 */
	WSTokenRequester getWSTokenRequester();
}
