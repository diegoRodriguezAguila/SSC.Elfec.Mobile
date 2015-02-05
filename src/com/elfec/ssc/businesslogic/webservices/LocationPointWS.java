package com.elfec.ssc.businesslogic.webservices;

import java.util.List;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllLocationPointsWSConverter;

/**
 * Se encarga de la conexi�n a los servicios web para puntos de pago
 * @author Diego
 *
 */
public class LocationPointWS {

	/**
	 * Obtiene todos los puntos de pago activos
	 * @param eventHandler
	 */
	public void getAllLocationPoints(IWSFinishEvent<List<LocationPoint>> eventHandler)
	{
		WebServiceConnector<List<LocationPoint>> paypointWSConnector = 
				new WebServiceConnector<List<LocationPoint>>("LocationPointWS.php?wsdl", "", 
						"ssc_elfec", "GetAllLocationPoints", new GetAllLocationPointsWSConverter(), eventHandler);
		paypointWSConnector.execute();
	}
}
