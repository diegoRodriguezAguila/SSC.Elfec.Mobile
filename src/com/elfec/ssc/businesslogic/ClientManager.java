package com.elfec.ssc.businesslogic;

import org.joda.time.DateTime;

import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.ClientStatus;

/**
 * Se encarga de las distintas operaciones relacionadas con los clientes
 * @author Diego
 */
public class ClientManager {

	/**
	 * Registra a un cliente en la base de datos con el gmail proporcionado y
	 * con el estado activo por defecto
	 * @param gmail
	 */
	public static void RegisterClient(String gmail)
	{
		Client client = new Client(gmail, ClientStatus.ACTIVE);
		client.setInsertDate(DateTime.now());
		client.save();
	}
}
