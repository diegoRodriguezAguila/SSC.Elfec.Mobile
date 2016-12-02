package com.elfec.ssc.web_services;

import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSParam;

import java.util.List;

import rx.Observable;

/**
 * Se encarga de la conexión a los servicios web para puntos de pago
 *
 * @author Diego
 */
public class UsageService {

    private SscToken sscToken;

    public UsageService(SscToken sscToken) {
        this.sscToken = sscToken;
    }

    /**
     * Gets all the usages of an account
     * @param nus Account's NUS
     */
    public Observable<List<Usage>> getUsages(String nus) {
        return new ServiceConnector<List<Usage>>("AccountWS.php?wsdl",
                "GetUsage", sscToken){}
                .execute(new WSParam("NUS", nus));
    }
}
