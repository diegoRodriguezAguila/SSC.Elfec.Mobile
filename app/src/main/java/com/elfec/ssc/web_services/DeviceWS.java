package com.elfec.ssc.web_services;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.UpdateDeviceGCMTokenConverter;

/**
 * Se encarga de la conexi�n a los servicios web para dispositivos
 * @author Diego
 *
 */
public class DeviceWS {
	private SscToken sscToken;
	
	public DeviceWS(SscToken sscToken){
		this.sscToken = sscToken;
	}

	/**
	 * Registra una cuenta por medio de servicios web
	 * @param lastToken ultimo token registrado
	 * @param Imei Imei dispositivo
	 * @param newToken nuevo token
	 * @param eventHandler handler del evento
	 */
	public void updateDeviceGCMToken(String lastToken, String Imei, String newToken, IWSFinishEvent<Boolean> eventHandler )
	{
		WebServiceConnector<Boolean> accountWSConnector = 
				new WebServiceConnector<>("DeviceWS.php?wsdl", "",
						"ssc_elfec", "UpdateDeviceGCMToken", sscToken, new UpdateDeviceGCMTokenConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("LastToken", lastToken), new WSParam("IMEI", Imei), new WSParam("NewToken", newToken));
	}
}
