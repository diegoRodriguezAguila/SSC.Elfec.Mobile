package com.elfec.ssc.presenter.views;

import com.elfec.ssc.helpers.PreferencesManager;

public interface IMainMenu {

	/**
	 * Invoca los metodos necesarios para cambiar a la vista de ver las cuentas
	 */
	public void goToViewAccounts();
	/**
	 * Obtiene el PreferencesManager con el contexto de la aplicación global
	 * @return {@link PreferencesManager}
	 */
	public PreferencesManager getPreferences();
	/**
	 * Advierte al usuario que no puede utilizar una funcionalidad 
	 * por que no tiene una cuenta de gmail registrada
	 */
	public void warnUserHasNoAccounts();
}
