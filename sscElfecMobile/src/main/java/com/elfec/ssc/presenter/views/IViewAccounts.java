package com.elfec.ssc.presenter.views;

import com.elfec.ssc.model.Account;

import java.util.List;

public interface IViewAccounts {
	/**
	 * Muestra una lista de cuentas, si se pasa null o una lista vacia
	 * se muestra un mensaje por defecto al usuario de que no hay cuentas
	 * @param accounts lista de cuentas
	 */
	void showAccounts(List<Account> accounts);

	/**
	 * Obtiene el IMEI del dispositivo
	 * @return imei dispositivo
	 */
	String getImei();
	/**
	 * Muestra un mensaje de que la cuenta se borró exitosamente
	 */
	void showAccountDeleted();
	/**
	 * Muestra errores al usuario
	 * @param errors errores
	 */
	void displayErrors(List<Exception> errors);
	void dialogRemove(int position); 
	/**
	 * Muestra un dialogo de espera
	 */
	void showWaiting();
	/**
	 * Oculta el dialogo de espera
	 */
	void hideWaiting();
	/**
	 * Muestra errores que podrían ocurrir al obtener las cuentas
	 * @param errors errores
	 */
	void showViewAccountsErrors(List<Exception> errors);

}
