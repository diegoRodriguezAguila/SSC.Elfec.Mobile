package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.WSCredential;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.RequestWSTokenConverter;

import java.security.InvalidParameterException;

public class TokenWS {

	private WSCredential credentials;
	
	public TokenWS(WSCredential credentials){
		if(credentials==null)
			throw new InvalidParameterException("WSCredentials cannot be null");
		this.credentials = credentials;
	}
	
	/**
	 * Obtiene el WSToken para el dispositivo, a partir de los credenciales 
	 * provistos
	 * @param eventHandler
	 */
	public void requestWSToken(IWSFinishEvent<WSToken> eventHandler)
	{
		WebServiceConnector<WSToken> paypointWSConnector = 
				new WebServiceConnector<>("TokenWS.php?wsdl", "",
						"ssc_elfec", "RequestToken", new RequestWSTokenConverter(), eventHandler);
		paypointWSConnector.execute(new WSParam("IMEI", credentials.getImei()), new WSParam("Signature", credentials.getSignature()),
				new WSParam("Salt", credentials.getSalt()), new WSParam("VersionCode", credentials.getVersionCode()));
	}
}
