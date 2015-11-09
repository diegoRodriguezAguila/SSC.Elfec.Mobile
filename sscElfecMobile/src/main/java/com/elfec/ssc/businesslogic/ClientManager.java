package com.elfec.ssc.businesslogic;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.enums.ClientStatus;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
/**
 * Se encarga de las distintas operaciones relacionadas con los clientes
 * @author Diego
 */
public class ClientManager {

	/**
	 * Registra a un cliente en la base de datos con el gmail proporcionado y
	 * con el estado activo por defecto, si el cliente ya existia cambia su estado.
	 * También cambia el estado de los demas clientes a inactivo
	 * @param gmail
	 */
	public static void registerActiveClient(String gmail)
	{
		Client currentActiveClient = Client.getActiveClient();
		if(currentActiveClient!=null)
		{
			currentActiveClient.setStatus(ClientStatus.REGISTERED);
			currentActiveClient.setUpdateDate(DateTime.now());
			currentActiveClient.save();
		}
		Client client = Client.getClientByGmail(gmail);
		if(client==null)
		{
			client = new Client(gmail, ClientStatus.ACTIVE);
			client.setInsertDate(DateTime.now());
		}
		else
		{
			client.setStatus(ClientStatus.ACTIVE);
			client.setUpdateDate(DateTime.now());
		}
		client.save();
	}
	/**
	 * Registra una lista de cuentas de un cliente
	 * @param accounts
	 * @return lista de cuentas registradas
	 */
	public static List<Account> registerClientAccounts(final List<Account> accounts)
	{
		List<Account> regAccounts = new ArrayList<Account>(accounts.size());
		for(Account account : accounts)
		{
			regAccounts.add(ElfecAccountsManager.registerAccount(account));
		}
		return regAccounts;
	}
}
