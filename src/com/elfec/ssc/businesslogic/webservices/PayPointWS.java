package com.elfec.ssc.businesslogic.webservices;

import java.util.List;

import com.elfec.ssc.model.PayPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WebServiceConnector;
import com.elfec.ssc.model.webservices.converters.GetAllPayPointsConverter;

/**
 * Se encarga de la conexión a los servicios web para puntos de pago
 * @author Diego
 *
 */
public class PayPointWS {

	/**
	 * Obtiene todos los puntos de pago activos
	 * @param eventHandler
	 */
	public void getAllPayPoints(IWSFinishEvent<List<PayPoint>> eventHandler)
	{
		WebServiceConnector<List<PayPoint>> paypointWSConnector = 
				new WebServiceConnector<List<PayPoint>>("http://192.168.12.81/SSC.Elfec/web_services/PayPointWS.php?wsdl", "", 
						"ssc_elfec", "GetAllPayPoints", new GetAllPayPointsConverter(), eventHandler);
		paypointWSConnector.execute();
	}
}
