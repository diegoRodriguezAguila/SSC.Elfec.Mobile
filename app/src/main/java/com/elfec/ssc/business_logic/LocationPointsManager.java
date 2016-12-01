package com.elfec.ssc.business_logic;

import android.location.Location;

import com.elfec.ssc.local_storage.LocationPointStorage;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.web_services.LocationPointService;
import com.elfec.ssc.web_services.SscTokenRequester;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


public class LocationPointsManager {

    /**
     * Retrieves de location points from web service and saves it for further uses
     *
     * @return observable of list of location points
     */
    public static Observable<List<LocationPoint>> syncLocationPoints() {
        return getLocationPointsFromWs()
                .flatMap(locationPoints -> new LocationPointStorage()
                        .saveLocationPoints(locationPoints));
    }

    /**
     * Gets the location points via web services
     * @return observable of list of location points
     */
    public static Observable<List<LocationPoint>> getLocationPointsFromWs(){
        return new SscTokenRequester().getSscToken()
                .flatMap(sscToken ->
                        new LocationPointService(sscToken).getLocationPoints());
    }

    
    /**
     * Registra el conjunto de puntos de ubicación
     *
     * @param points
     */
    public static void registerLocations(final List<LocationPoint> points) {
        for (LocationPoint point : points) {
            if (!LocationPoint.existPoint(point)) {
                point.setInsertDate(DateTime.now());
                point.save();
            }
        }
    }

    /**
     * Registra el conjunto de puntos de ubicación
     *
     * @param points
     */
    public static void removeLocations(final List<LocationPoint> points) {
        List<LocationPoint> allPoints = LocationPoint.getAll(LocationPoint.class);
        for (LocationPoint point : allPoints) {
            if (notExists(point, points)) {
                point.delete();
            }
        }
    }

    private static boolean notExists(LocationPoint point,
                                     List<LocationPoint> points) {
        for (LocationPoint p : points) {
            if (p.compare(point))
                return false;
        }
        return true;
    }

    /**
     * Obtiene los puntos cercanos a la ubicación dada, y con la distancia definida
     *
     * @param points
     * @param current
     * @param maxDistance
     * @return
     */
    public static List<LocationPoint> getNearestPoints(List<LocationPoint> points, Location current, double maxDistance) {
        List<LocationPoint> result = new ArrayList<>();
        for (LocationPoint point : points) {
            if (point.distanceFrom(current) <= maxDistance) {
                result.add(point);
            }
        }
        return result;
    }
}
