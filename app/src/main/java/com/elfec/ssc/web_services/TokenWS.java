package com.elfec.ssc.web_services;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.security.SscCredential;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.RequestWSTokenConverter;

import java.security.InvalidParameterException;

public class TokenWS {

	private SscCredential credentials;
	
	public TokenWS(SscCredential credentials){
		if(credentials==null)
			throw new InvalidParameterException("WSCredentials cannot be null");
		this.credentials = credentials;
	}
	
	/**
	 * Obtiene el SscToken para el dispositivo, a partir de los credenciales
	 * provistos
	 * @param eventHandler handler del evento
	 */
	public void requestSscToken(IWSFinishEvent<SscToken> eventHandler)
	{
		WebServiceConnector<SscToken> payPointWSConnector =
				new WebServiceConnector<>("TokenWS.php?wsdl", "",
						"ssc_elfec", "RequestToken", new RequestWSTokenConverter(), eventHandler);
		payPointWSConnector.execute(new WSParam("IMEI", credentials.getImei()), new WSParam("Signature", credentials.getSignature()),
				new WSParam("Salt", credentials.getSalt()), new WSParam("VersionCode", credentials.getVersionCode()));
	}
}
