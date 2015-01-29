package com.elfec.ssc.businesslogic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.location.Location;

import com.elfec.ssc.model.LocationPoint;;

public class LocationManager {
	public static void registerLocations(final List<LocationPoint> points)
	{
		for(LocationPoint point : points)
		{
			point.setInsertDate(DateTime.now());
			point.save();
		}
	}
	public static List<LocationPoint> getNearestPoints(List<LocationPoint> points,Location current,double maxDistance)
	{
		List<LocationPoint> result=new ArrayList<LocationPoint>();
		for(LocationPoint point:points)
		{
			if(point.distanceFrom(current)<=maxDistance)
			{
				result.add(point);
			}
		}
		return result;
	}
}
