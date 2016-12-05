package com.elfec.ssc.presenter.views;

import com.elfec.ssc.model.Account;

import java.util.List;

/***
 * Provee de una abstracción de la vista de registrar cuentas
 * @author Diego
 *
 */
 public interface IRegisterAccount extends IProcessView<Account> {
	 String getNus();
	 String getNusValidationRules();
	 void setNusErrors(List<String> validationErrors);
	 String getAccountNumber();
	 String getAccountNumberValidationRules();
	 void setAccountNumberErrors(List<String> validationErrors);
	 void notifyFieldErrors();
}
