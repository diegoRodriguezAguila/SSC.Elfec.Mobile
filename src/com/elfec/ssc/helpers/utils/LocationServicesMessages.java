package com.elfec.ssc.helpers.utils;

import java.util.Locale;

import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;

public class LocationServicesMessages {

	private static final String showingMsg = "Mostrando ";
	private static final String nearMsg = " cercan";
	private static final String endingFemenineMsg = "as";
	private static final String endingMasculineMsg = "os";
	private static final String allFemenineMsg = "todas las ";
	private static final String allMasculineMsg = "todos los ";
	
	public static String buildMessage(LocationPointType type, LocationDistance distance)
	{
		StringBuilder str = new StringBuilder(showingMsg);
		if(distance==LocationDistance.ALL)
		{
			str.append(type==LocationPointType.PAYPOINT?allMasculineMsg:allFemenineMsg);
		}
		str.append(type.toString().toLowerCase(Locale.getDefault()));
		if(distance==LocationDistance.NEAREST)
		{
			str.append(nearMsg).append(type==LocationPointType.OFFICE?endingFemenineMsg:endingMasculineMsg);
		}
		return str.toString();
	}
}
