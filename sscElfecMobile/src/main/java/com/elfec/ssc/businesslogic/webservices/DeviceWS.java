package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.UpdateDeviceGCMTokenConverter;

/**
 * Se encarga de la conexi�n a los servicios web para dispositivos
 * @author Diego
 *
 */
public class DeviceWS {
	private WSToken wsToken;
	
	public DeviceWS(WSToken wsToken){
		this.wsToken = wsToken;
	}

	/**
	 * Registra una cuenta por medio de servicios web
	 * @param lastToken
	 * @param IMEI
	 * @param newToken
	 * @param eventHandler
	 */
	public void updateDeviceGCMToken(String lastToken, String IMEI, String newToken, IWSFinishEvent<Boolean> eventHandler )
	{
		WebServiceConnector<Boolean> accountWSConnector = 
				new WebServiceConnector<Boolean>("DeviceWS.php?wsdl", "", 
						"ssc_elfec", "UpdateDeviceGCMToken", wsToken, new UpdateDeviceGCMTokenConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("LastToken", lastToken), new WSParam("IMEI", IMEI), new WSParam("NewToken", newToken));
	}
}
