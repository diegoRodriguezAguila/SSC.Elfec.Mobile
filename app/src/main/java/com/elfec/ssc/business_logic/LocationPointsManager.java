package com.elfec.ssc.business_logic;

import android.location.Location;

import com.elfec.ssc.model.LocationPoint;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

;

public class LocationPointsManager {
	/**
	 * Registra el conjunto de puntos de ubicación
	 * @param points
	 */
	public static void registerLocations(final List<LocationPoint> points)
	{
		for(LocationPoint point : points)
		{
			if(!LocationPoint.existPoint(point))
			{
				point.setInsertDate(DateTime.now());
				point.save();
			}
		}
	}
	
	/**
	 * Registra el conjunto de puntos de ubicación
	 * @param points
	 */
	public static void removeLocations(final List<LocationPoint> points)
	{
		List<LocationPoint> allPoints = LocationPoint.getAll(LocationPoint.class);
		for(LocationPoint point : allPoints)
		{
			if(notExists(point,points))
			{
				point.delete();
			}
		}
	}
	
	private static boolean notExists(LocationPoint point,
			List<LocationPoint> points) {
		for(LocationPoint p : points)
		{
			if(p.compare(point))
				return false;
			
		}
		return true;
	}

	/**
	 * Obtiene los puntos cercanos a la ubicación dada, y con la distancia definida
	 * @param points
	 * @param current
	 * @param maxDistance
	 * @return
	 */
	public static List<LocationPoint> getNearestPoints(List<LocationPoint> points,Location current,double maxDistance)
	{
		List<LocationPoint> result=new ArrayList<>();
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
