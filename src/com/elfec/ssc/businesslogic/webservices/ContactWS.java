package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.Contact;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetContactUpdateWSConverter;

/**
 * Se encarga de la conexión a los servicios web para contactos
 * @author Diego
 *
 */
public class ContactWS {

	/**
	 * Obtiene una actualización de la información de contacto
	 * @param eventHandler
	 */
	public void getContactUpdate(IWSFinishEvent<Contact> eventHandler)
	{
		WebServiceConnector<Contact> accountWSConnector = 
				new WebServiceConnector<Contact>("http://192.168.12.81/SSC.Elfec/web_services/ContactWS.php?wsdl", "", 
						"ssc_elfec", "GetContactUpdate", new GetContactUpdateWSConverter(), eventHandler);
		accountWSConnector.execute();
	}
}
