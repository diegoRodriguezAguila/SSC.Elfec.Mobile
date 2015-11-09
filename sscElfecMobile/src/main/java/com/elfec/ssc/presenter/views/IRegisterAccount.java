package com.elfec.ssc.presenter.views;

import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.security.PreferencesManager;

import java.util.List;

/***
 * Provee de una abstracci�n de la vista de registrar cuentas
 * @author Diego
 *
 */
 public interface IRegisterAccount {
	 String getNUS();
	 String getNUSValidationRules();
	 void setNUSErrors(List<String> validationErrors);
	 String getAccountNumber();
	 String getAccountNumberValidationRules();
	 void setAccountNumberErrors(List<String> validationErrors);
	 String getIMEI();
	 String getPhoneNumber();
	 void notifyAccountSuccessfulyRegistered();
	 void showWSWaiting();
	 void hideWSWaiting();
	 void notifyAccountAlreadyRegistered();
	 void notifyErrorsInFields();
	 void showRegistrationErrors(List<Exception> errors);
	/**
	 * Obtiene el PreferencesManager con el contexto de la aplicaci�n global
	 * @return {@link PreferencesManager}
	 */
	 PreferencesManager getPreferences();
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
