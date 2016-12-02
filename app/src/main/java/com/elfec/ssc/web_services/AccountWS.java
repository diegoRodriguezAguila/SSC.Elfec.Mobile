package com.elfec.ssc.web_services;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllAccountsWSConverter;
import com.elfec.ssc.model.webservices.converters.RegisterAccountWSConverter;
import com.elfec.ssc.model.webservices.converters.RemoveAccountWSConverter;

import java.util.List;
/**
 * Se encarga de la conexión a los servicios web para cuentas
 * @author Diego
 *
 */
public class AccountWS 
{
	private SscToken sscToken;
	
	public AccountWS(SscToken sscToken){
		this.sscToken = sscToken;
	}
	
	/**
	 * Registra una cuenta por medio de servicios web
	 * @param accountNumber número de cuenta
	 * @param nus nus
	 * @param gmail gmail
	 * @param phoneNumber numero de telefono
	 * @param deviceBrand marca dispositivo
	 * @param deviceModel modelo dispositivo
	 * @param deviceImei Imei dispositivo
	 * @param gcmToken gcm token
	 */
	public void registerAccount(String accountNumber, String nus, String gmail, String phoneNumber,
			String deviceBrand, String deviceModel, String deviceImei, String gcmToken, IWSFinishEvent<Account> eventHandler )
	{
		WebServiceConnector<Account> accountWSConnector = 
				new WebServiceConnector<>("AccountWS.php?wsdl", "",
						"ssc_elfec", "RegisterAccount", sscToken, new RegisterAccountWSConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("AccountNumber", accountNumber), new WSParam("NUS", nus), new WSParam("GMail", gmail),
				new WSParam("PhoneNumber", phoneNumber), new WSParam("DeviceBrand", deviceBrand), new WSParam("DeviceModel", deviceModel),
				new WSParam("DeviceIMEI", deviceImei), new WSParam("GCM", gcmToken));
	}
	
	/**
	 * Obtiene todas las cuentas que se hayan asignado a un gmail determinado
	 * @param gmail gmail
	 * @param eventHandler handler
	 */
	public void getAllAccounts(String gmail, String deviceBrand, String deviceModel, String deviceIMEI,
			String gCMtoken, IWSFinishEvent<List<Account>> eventHandler)
	{
		WebServiceConnector<List<Account>> accountWSConnector = 
				new WebServiceConnector<>("AccountWS.php?wsdl", "",
						"ssc_elfec", "GetAllAccounts", sscToken, new GetAllAccountsWSConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("GMail", gmail), new WSParam("DeviceBrand", deviceBrand), new WSParam("DeviceModel", deviceModel),
				new WSParam("DeviceIMEI", deviceIMEI), new WSParam("GCM", gCMtoken));
	}
	
	/**
	 * Elimina la cuenta que corresponde a los parametros, por medio de servicios web
	 * @param gmail gmail
	 * @param nus nus
	 * @param imei Imei
	 * @param eventHandler handler
	 */
	public void removeAccount(String gmail,String nus,String imei,IWSFinishEvent<Boolean> eventHandler)
	{
		WebServiceConnector<Boolean> accountWSConnector = 
				new WebServiceConnector<>("AccountWS.php?wsdl", "",
						"ssc_elfec", "DeleteAccount", sscToken, new RemoveAccountWSConverter(), eventHandler);
		accountWSConnector.execute(new WSParam("IMEI", imei),new WSParam("NUS", nus),new WSParam("GMail", gmail));

	}

}
