package com.elfec.ssc.businesslogic.webservices;

import java.util.List;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllAccountsWSConverter;
import com.elfec.ssc.model.webservices.converters.RegisterAccountWSConverter;
import com.elfec.ssc.model.webservices.converters.RemoveAccountWSConverter;
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
	public void registerAccount(String accountNumber, String nUS, String gmail, String phoneNumber, 
			String deviceBrand, String deviceModel, String deviceIMEI, String gCMtoken, IWSFinishEvent<Boolean> eventHandler )
	{
		WebServiceConnector<Boolean> accountWSConnector = 
				new WebServiceConnector<Boolean>("http://192.168.12.81/SSC.Elfec/web_services/AccountWS.php?wsdl", "", 
						"ssc_elfec", "RegisterAccount", new RegisterAccountWSConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("AccountNumber", accountNumber), new WSParam("NUS", nUS), new WSParam("GMail", gmail), 
				new WSParam("PhoneNumber", phoneNumber), new WSParam("DeviceBrand", deviceBrand), new WSParam("DeviceModel", deviceModel),
				new WSParam("DeviceIMEI", deviceIMEI), new WSParam("GCM", gCMtoken));
	}
	
	/**
	 * Obtiene todas las cuentas que se hayan asignado a un gmail determinado
	 * @param gmail
	 * @param eventHandler
	 */
	public void getAllAccounts(String gmail, String deviceBrand, String deviceModel, String deviceIMEI,
			String gCMtoken, IWSFinishEvent<List<Account>> eventHandler)
	{
		WebServiceConnector<List<Account>> accountWSConnector = 
				new WebServiceConnector<List<Account>>("http://192.168.12.81/SSC.Elfec/web_services/AccountWS.php?wsdl", "", 
						"ssc_elfec", "GetAllAccounts", new GetAllAccountsWSConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("GMail", gmail), new WSParam("DeviceBrand", deviceBrand), new WSParam("DeviceModel", deviceModel),
				new WSParam("DeviceIMEI", deviceIMEI), new WSParam("GCM", gCMtoken));
	}
	
	/**
	 * Elimina la cuenta que corresponde a los parametros, por medio de servicios web
	 * @param gmail
	 * @param nus
	 * @param imei
	 * @param eventHandler
	 */
	public void removeAccount(String gmail,String nus,String imei,IWSFinishEvent<Boolean> eventHandler)
	{
		WebServiceConnector<Boolean> accountWSConnector = 
				new WebServiceConnector<Boolean>("http://192.168.12.81/SSC.Elfec/web_services/AccountWS.php?wsdl", "", 
						"ssc_elfec", "DeleteAccount", new RemoveAccountWSConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("IMEI", imei),new WSParam("NUS", nus),new WSParam("GMail", gmail));

	}

}
