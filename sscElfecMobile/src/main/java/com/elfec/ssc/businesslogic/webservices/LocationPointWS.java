package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllLocationPointsWSConverter;

import java.util.List;

/**
 * Se encarga de la conexi�n a los servicios web para puntos de pago
 * @author Diego
 *
 */
public class LocationPointWS {

	private SscToken sscToken;
	
	public LocationPointWS(SscToken sscToken){
		this.sscToken = sscToken;
	}
	/**
	 * Obtiene todos los puntos de pago activos
	 * @param eventHandler handler del evento
	 */
	public void getAllLocationPoints(IWSFinishEvent<List<LocationPoint>> eventHandler)
	{
		WebServiceConnector<List<LocationPoint>> paypointWSConnector = 
				new WebServiceConnector<>("LocationPointWS.php?wsdl", "",
						"ssc_elfec", "GetAllLocationPoints", sscToken, new GetAllLocationPointsWSConverter(), eventHandler);
		paypointWSConnector.execute();
	}
}
