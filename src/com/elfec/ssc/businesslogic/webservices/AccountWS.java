package com.elfec.ssc.businesslogic.webservices;

import java.util.List;

import com.elfec.ssc.model.webservices.RegisterAccountWSConverter;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
/**
 * Se encarga de la conexión a los servicios web para cuentas
 * @author Diego
 *
 */
public class AccountWS 
{
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
	public List<Integer> registerAccount(String accountNumber, String nUS, String gmail, String phoneNumber, 
			String deviceBrand, String deviceModel, String deviceIMEI, String gCMtoken)
	{
		WebServiceConnector<List<Integer>> accountWS = 
				new WebServiceConnector<List<Integer>>("http://192.168.12.81/SSC.Elfec/web_services/AccountWS.php?wsdl", "", 
						"ssc_elfec", "RegisterAccount", new RegisterAccountWSConverter());
		accountWS.execute(new WSParam("AccountNumber", accountNumber), new WSParam("NUS", nUS), new WSParam("GMail", gmail), 
				new WSParam("PhoneNumber", phoneNumber), new WSParam("DeviceBrand", deviceBrand), new WSParam("DeviceModel", deviceModel),
				new WSParam("DeviceIMEI", deviceIMEI), new WSParam("GCM", gCMtoken));
		return null;
	}

}
