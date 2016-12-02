package com.elfec.ssc.web_services;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.security.SscToken;

import java.util.List;

import rx.Observable;

/**
 * Se encarga de la conexión a los servicios web para puntos de pago
 *
 * @author Diego
 */
public class LocationPointService {

    private SscToken sscToken;

    public LocationPointService(SscToken sscToken) {
        this.sscToken = sscToken;
    }

    /**
     * Obtiene todos los puntos de pago activos
     */
    public Observable<List<LocationPoint>> getLocationPoints() {
        return new ServiceConnector<List<LocationPoint>>("LocationPointWS.php?wsdl",
                "GetAllLocationPoints", sscToken){}
                .execute();
    }
}
