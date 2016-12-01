package com.elfec.ssc.web_services;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.LocationPointsConverter;

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
     *
     * @param eventHandler handler del evento
     */
    public void getAllLocationPoints(IWSFinishEvent<List<LocationPoint>> eventHandler) {
        WebServiceConnector<List<LocationPoint>> paypointWSConnector =
                new WebServiceConnector<>("LocationPointService.php?wsdl", "",
                        "ssc_elfec", "GetAllLocationPoints", sscToken,
                        new LocationPointsConverter(), eventHandler);
        paypointWSConnector.execute();
    }

    /**
     * Obtiene todos los puntos de pago activos
     */
    public Observable<List<LocationPoint>> getLocationPoints() {
        return new ServiceConnector<>("LocationPointService.php?wsdl",
                "GetAllLocationPoints", sscToken, new LocationPointsConverter())
                .execute();
    }
}
