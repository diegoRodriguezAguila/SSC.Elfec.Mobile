package com.elfec.ssc.web_services;

import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSParam;

import rx.Observable;

/**
 * Se encarga de la conexión a los servicios web para dispositivos
 *
 * @author Diego
 */
public class DeviceService {
    private SscToken sscToken;

    public DeviceService(SscToken sscToken) {
        this.sscToken = sscToken;
    }

    /**
     * Registra una cuenta por medio de servicios web
     *
     * @param Imei     Imei dispositivo
     * @param newToken nuevo token
     */
    public Observable<Boolean> updateGcmToken(String Imei, String newToken) {
        return new ServiceConnector<Boolean>("DeviceWS.php?wsdl",
                "UpdateDeviceGCMToken", sscToken) {
        }.execute(new WSParam("LastToken", ""),
                new WSParam("IMEI", Imei),
                new WSParam("NewToken", newToken));
    }
}
