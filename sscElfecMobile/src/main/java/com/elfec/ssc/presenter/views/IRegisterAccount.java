package com.elfec.ssc.presenter.views;

import java.util.List;

/***
 * Provee de una abstracción de la vista de registrar cuentas
 * @author Diego
 *
 */
 public interface IRegisterAccount {
	 String getNus();
	 String getNusValidationRules();
	 void setNusErrors(List<String> validationErrors);
	 String getAccountNumber();
	 String getAccountNumberValidationRules();
	 void setAccountNumberErrors(List<String> validationErrors);
	 String getImei();
	 String getPhoneNumber();
	 void notifyAccountSuccessfullyRegistered();
	 void showWSWaiting();
	 void hideWSWaiting();
	 void notifyAccountAlreadyRegistered();
	 void notifyErrorsInFields();
	 void showRegistrationErrors(List<Exception> errors);
}
