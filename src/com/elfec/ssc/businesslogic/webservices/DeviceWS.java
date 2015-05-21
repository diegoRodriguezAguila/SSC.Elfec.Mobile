package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.UpdateDeviceGCMTokenConverter;

/**
 * Se encarga de la conexión a los servicios web para dispositivos
 * @author Diego
 *
 */
public class DeviceWS {
	/**
	 * Registra una cuenta por medio de servicios web
	 * @param accountNumber
	 * @param nUS
	 * @param gmail
	 * @param phoneNumber
	 * @param deviceBrand
	 * @param deviceModel
	 * @param deviceIMEI
	 * @param gCMtoken
	 * @return
	 */
	public void updateDeviceGCMToken(String lastToken, String IMEI, String newToken, IWSFinishEvent<Boolean> eventHandler )
	{
		WebServiceConnector<Boolean> accountWSConnector = 
				new WebServiceConnector<Boolean>("DeviceWS.php?wsdl", "", 
						"ssc_elfec", "UpdateDeviceGCMToken", new UpdateDeviceGCMTokenConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("LastToken", lastToken), new WSParam("IMEI", IMEI), new WSParam("NewToken", newToken));
	}
}
