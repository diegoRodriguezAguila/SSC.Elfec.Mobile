package com.elfec.ssc.businesslogic;

import org.joda.time.DateTime;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;

/**
 * Se encarga de las distintas operaciones relacionadas con las cuentas de elfec
 * @author Diego
 */
public class ElfecAccountsManager {

	/**
	 * Registra una cuenta de elfec  en la base de datos asignando al ownerClient como dueño de la cuenta y 
	 * con el nus y numero de cuentas proporcionados y con estado 1 por defecto
	 * @param ownerClient
	 * @param accountNumber
	 * @param nus
	 */
	public static void RegisterAccount(Client ownerClient, String accountNumber,String nus) {
		Account account = new Account(ownerClient, accountNumber, nus);
		account.setInsertDate(DateTime.now());
		account.save();
	}

}
