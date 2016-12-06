package com.elfec.ssc.web_services;

import com.elfec.ssc.model.security.SscCredential;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSParam;

import java.security.InvalidParameterException;

import rx.Observable;

public class TokenService {

    private SscCredential credentials;

    public TokenService(SscCredential credentials) {
        if (credentials == null)
            throw new InvalidParameterException("SscCredentials can't be null");
        this.credentials = credentials;
    }

    /**
     * Obtiene el SscToken para el dispositivo, a partir de los credenciales
     * provistos
     */
    public Observable<SscToken> requestSscToken() {
        return new ServiceConnector<SscToken>("TokenWS.php?wsdl", "RequestToken"){}
                .execute(new WSParam("IMEI", credentials.getImei()), 
                        new WSParam("Signature", credentials.getSignature()),
                new WSParam("Salt", credentials.getSalt()), new WSParam("VersionCode", 
                                credentials.getVersionCode()));
    }
}
