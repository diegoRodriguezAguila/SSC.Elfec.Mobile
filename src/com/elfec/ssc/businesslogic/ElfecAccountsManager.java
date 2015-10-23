package com.elfec.ssc.businesslogic;

import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.ActiveAndroid;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Usage;

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
	 * Registra una cuenta de elfec  en la base de datos asignando al ownerClient como dueño de la cuenta y 
	 * con el nus y numero de cuentas proporcionados y con estado 1 por defecto
	 * @param ownerClient
	 * @param accountNumber
	 * @param nus
	 * @return retorna la cuenta retistrada
	 */
	public static Account registerAccount(Account account) {
		Account newAccount = Account.findAccount(account.getClient().getGmail(), account.getNUS(), account.getAccountNumber());
		if(newAccount==null)
		{
			newAccount = account;
			newAccount.setInsertDate(DateTime.now());
		}
		else 
		{
			newAccount.addDebts(account.getDebts());
			newAccount.setStatus((short) 1);
			
		}
		newAccount.save();
		List<Debt> accountDebts = newAccount.getDebts();
		for(Debt debt : accountDebts)
		{
			if(debt.getInsertDate()==null)
				debt.setInsertDate(DateTime.now());
			debt.save();
		}
		return newAccount;
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
	
	/**
	 * Registra una lista de consumos a nombre de una cuenta
	 * @param account
	 * @param usages
	 */
	public static void registerAccountUsages(Account account, List<Usage> usages)
	{
		ActiveAndroid.beginTransaction();
		try
		{
			for (Usage usage : usages) {
				usage.setAccount(account);
				usage.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			ActiveAndroid.endTransaction();
		}
	}

}
