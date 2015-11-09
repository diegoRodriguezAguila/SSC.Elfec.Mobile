package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.Contact;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetContactUpdateWSConverter;

/**
 * Se encarga de la conexión a los servicios web para contactos
 * @author Diego
 *
 */
public class ContactWS {

	private WSToken wsToken;
	
	public ContactWS(WSToken wsToken){
		this.wsToken = wsToken;
	}
	/**
	 * Obtiene una actualización de la información de contacto
	 * @param eventHandler
	 */
	public void getContactUpdate(IWSFinishEvent<Contact> eventHandler)
	{
		WebServiceConnector<Contact> accountWSConnector = 
				new WebServiceConnector<Contact>("ContactWS.php?wsdl", "", 
						"ssc_elfec", "GetContactUpdate", wsToken, new GetContactUpdateWSConverter(), eventHandler);
		accountWSConnector.execute();
	}
}
