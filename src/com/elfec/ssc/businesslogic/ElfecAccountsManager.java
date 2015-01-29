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
	public static void registerAccount(Client ownerClient, String accountNumber,String nus) {
		Account newAccount = Account.findAccount(ownerClient.getGmail(), nus, accountNumber);
		if(newAccount==null)
		{
			newAccount = new Account(ownerClient, accountNumber, nus);
			newAccount.setInsertDate(DateTime.now());
		}
		else newAccount.setStatus((short) 1);
		newAccount.save();
	}
	
	/**
	 * Marca a la cuenta con el nus y cliente especificados como eliminada
	 * @return boolean, true si se logro eliminar
	 */
	public static boolean deleteAccount(String gmail,String nus)
	{	
		Account account = Account.findAccount(gmail, nus);
		if(account!=null)
		{
			account.setStatus((short) 0);
			return account.save()>0;
		}
		return false;
	}

}
