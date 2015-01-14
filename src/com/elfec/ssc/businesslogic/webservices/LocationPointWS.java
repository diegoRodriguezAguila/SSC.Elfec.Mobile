package com.elfec.ssc.businesslogic.webservices;

import java.util.List;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllLocationPointsConverter;

/**
 * Se encarga de la conexión a los servicios web para puntos de pago
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
				new WebServiceConnector<List<LocationPoint>>("http://192.168.12.81/SSC.Elfec/web_services/PayPointWS.php?wsdl", "", 
						"ssc_elfec", "GetAllPayPoints", new GetAllLocationPointsConverter(), eventHandler);
		paypointWSConnector.execute();
	}
}
