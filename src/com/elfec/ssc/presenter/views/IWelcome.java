package com.elfec.ssc.presenter.views;

import com.elfec.ssc.security.PreferencesManager;

public interface IWelcome {
	/**
	 * Invoca los metodos necesarios para cambiar a la vista del menú principal
	 */
	public void goToMainMenu();
	
	/**
	 * Obtiene el PreferencesManager con el contexto de la aplicación global
	 * @return {@link PreferencesManager}
	 */
	public PreferencesManager getPreferences();
}
