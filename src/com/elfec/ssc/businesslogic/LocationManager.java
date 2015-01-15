package com.elfec.ssc.businesslogic;

import java.util.List;

import org.joda.time.DateTime;

import com.elfec.ssc.model.LocationPoint;;

public class LocationManager {
	public static void RegisterLocations(final List<LocationPoint> points)
	{
		for(LocationPoint point : points)
		{
			point.setInsertDate(DateTime.now());
			point.save();
		}
	}
}
