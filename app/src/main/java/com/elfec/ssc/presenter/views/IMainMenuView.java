package com.elfec.ssc.presenter.views;

public interface IMainMenuView extends IBaseView {

	/**
	 * Invoca los metodos necesarios para cambiar a la vista de ver las cuentas
	 */
	void goToViewAccounts();
	/**
	 * Advierte al usuario que no puede utilizar una funcionalidad 
	 * por que no tiene una cuenta de gmail registrada
	 */
	void warnUserHasNoAccounts();
	/**
	 * Muestra el cliente actual
	 * @param gmail
	 */
	void setCurrentClient(String gmail);
}
