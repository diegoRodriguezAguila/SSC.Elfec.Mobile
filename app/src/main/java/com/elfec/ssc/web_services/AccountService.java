package com.elfec.ssc.web_services;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Device;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.RemoveAccountWSConverter;

import java.util.List;

import rx.Observable;

/**
 * Se encarga de la conexión a los servicios web para cuentas
 *
 * @author Diego
 */
public class AccountService {
    private SscToken sscToken;

    public AccountService(SscToken sscToken) {
        this.sscToken = sscToken;
    }

    /**
     * Registra una cuenta por medio de servicios web
     *
     * @param accountNumber número de cuenta
     * @param nus           nus
     * @param gmail         gmail del cliente a nombre del cual se registrará la cuenta
     * @param device        device information
     */
    public Observable<Account> registerAccount(String accountNumber, String nus,
                                               String gmail, Device device) {
        return new ServiceConnector<Account>("AccountWS.php?wsdl",
                "RegisterAccount", sscToken) {
        }.execute(new WSParam("AccountNumber", accountNumber),
                new WSParam("NUS", nus), new WSParam("GMail", gmail),
                new WSParam("PhoneNumber", device.getPhoneNumber()),
                new WSParam("DeviceBrand", device.getBrand()),
                new WSParam("DeviceModel", device.getModel()),
                new WSParam("DeviceIMEI", device.getImei()),
                new WSParam("GCM", device.getGcmToken()));
    }


    /**
     * Obtiene todas las cuentas que se hayan asignado a un gmail determinado
     *
     * @param gmail  gmail
     * @param device device information
     * @return observable of account list
     */
    public Observable<List<Account>> getAccounts(String gmail, Device device) {
        return new ServiceConnector<List<Account>>("AccountWS.php?wsdl",
                "GetAllAccounts", sscToken) {
        }.execute(new WSParam("GMail", gmail),
                new WSParam("DeviceBrand", device.getBrand()),
                new WSParam("DeviceModel", device.getModel()),
                new WSParam("DeviceIMEI", device.getImei()),
                new WSParam("GCM", device.getGcmToken()));
    }

    /**
     * Elimina la cuenta que corresponde a los parametros, por medio de servicios web
     *
     * @param gmail        gmail
     * @param nus          nus
     * @param imei         Imei
     * @param eventHandler handler
     */
    public void removeAccount(String gmail, String nus, String imei, IWSFinishEvent<Boolean> eventHandler) {
        WebServiceConnector<Boolean> accountWSConnector =
                new WebServiceConnector<>("AccountWS.php?wsdl", "",
                        "ssc_elfec", "DeleteAccount", sscToken, new RemoveAccountWSConverter(), eventHandler);
        accountWSConnector.execute(new WSParam("IMEI", imei), new WSParam("NUS", nus), new WSParam("GMail", gmail));

    }

    /**
     * Elimina la cuenta que corresponde a los parametros, por medio de servicios web
     *
     * @param gmail gmail
     * @param nus   nus
     * @param imei  Imei
     */
    public Observable<Boolean> removeAccount(String gmail, String nus, String imei) {
        return new ServiceConnector<Boolean>("AccountWS.php?wsdl",
                "DeleteAccount", sscToken) {
        }.execute(new WSParam("IMEI", imei), new WSParam("NUS", nus),
                new WSParam("GMail", gmail));

    }

}
