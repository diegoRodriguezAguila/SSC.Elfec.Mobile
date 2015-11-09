package com.elfec.ssc.helpers.utils;

/**
 * Clase con m�todos para formatear una n�mero cuenta
 * @author drodriguez
 *
 */
public class AccountFormatter {

	/**
	 * Formatea una cuenta sin guiones a una con guiones con tama�o de 10 digitos fijo
	 * @param accountNumber
	 * @return
	 */
	public static String formatAccountNumber(String accountNumber)
	{
		StringBuilder account=new StringBuilder(accountNumber);
		if(account.length()==9)
		{
			account.insert(0, "0");
		}
		account.insert(2, "-");
		account.insert(6, "-");
		account.insert(10, "-");
		return account.toString();
	}
}
