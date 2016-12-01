package com.elfec.ssc.web_services;

import com.elfec.ssc.model.Contact;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetContactUpdateWSConverter;

/**
 * Se encarga de la conexión a los servicios web para contactos
 * @author Diego
 *
 */
public class ContactWS {

	private SscToken sscToken;
	
	public ContactWS(SscToken sscToken){
		this.sscToken = sscToken;
	}
	/**
	 * Obtiene una actualización de la información de contacto
	 * @param eventHandler
	 */
	public void getContactUpdate(IWSFinishEvent<Contact> eventHandler)
	{
		WebServiceConnector<Contact> accountWSConnector = 
				new WebServiceConnector<>("ContactWS.php?wsdl", "",
						"ssc_elfec", "GetContactUpdate", sscToken, new GetContactUpdateWSConverter(), eventHandler);
		accountWSConnector.execute();
	}
}
