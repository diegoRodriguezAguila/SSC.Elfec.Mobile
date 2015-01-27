package com.elfec.ssc.presenter.views;

import java.util.List;

/***
 * Provee de una abstracción de la vista de registrar cuentas
 * @author Diego
 *
 */
public interface IRegisterAccount {
	public String getNUS();
	public String getNUSValidationRules();
	public void setNUSErrors(List<String> validationErrors);
	public String getAccountNumber();
	public String getAccountNumberValidationRules();
	public void setAccountNumberErrors(List<String> validationErrors);
	public String getIMEI();
	public String getPhoneNumber();
	public String getGCM();
	public void notifyAccountSuccessfulyRegistered();
	public void showWSWaiting();
	public void hideWSWaiting();
	public void notifyAccountAlreadyRegistered();
	public void notifyErrorsInFields();
	public void showRegistrationErrors(List<Exception> errors);
}
