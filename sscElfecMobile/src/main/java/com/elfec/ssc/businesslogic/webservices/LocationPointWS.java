package com.elfec.ssc.businesslogic.webservices;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllLocationPointsWSConverter;

import java.util.List;

/**
 * Se encarga de la conexi�n a los servicios web para puntos de pago
 * @author Diego
 *
 */
public class LocationPointWS {

	private WSToken wsToken;
	
	public LocationPointWS(WSToken wsToken){
		this.wsToken = wsToken;
	}
	/**
	 * Obtiene todos los puntos de pago activos
	 * @param eventHandler
	 */
	public void getAllLocationPoints(IWSFinishEvent<List<LocationPoint>> eventHandler)
	{
		WebServiceConnector<List<LocationPoint>> paypointWSConnector = 
				new WebServiceConnector<>("LocationPointWS.php?wsdl", "",
						"ssc_elfec", "GetAllLocationPoints", wsToken, new GetAllLocationPointsWSConverter(), eventHandler);
		paypointWSConnector.execute();
	}
}
