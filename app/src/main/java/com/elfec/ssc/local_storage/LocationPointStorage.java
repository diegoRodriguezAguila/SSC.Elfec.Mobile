package com.elfec.ssc.local_storage;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.model.LocationPoint;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by drodriguez on 01/12/2016.
 * LocationPointStorage
 */

public class LocationPointStorage {

    private static final String LOCATION_POINTS_BOOK = "location_points.book";
    private static final String LOCATION_POINTS_KEY = "location_points";

    /**
     * Saves the location points to the database with all its subclasses, it doesn't execute
     * inmediately,
     * it creates an observable to be execute in the future
     *
     * @param locationPoints to save
     * @return Observable of  locationPoints
     */
    public Observable<List<LocationPoint>> saveLocationPoints(List<LocationPoint> locationPoints) {
        return RxPaper.book(LOCATION_POINTS_BOOK)
                .write(LOCATION_POINTS_KEY, locationPoints);
    }

    /**
     * Saves a location point to the database
     *
     * @param locationPoint to save
     * @return Observable of locationPoint
     */
    public Observable<LocationPoint> saveLocationPoint(LocationPoint locationPoint) {
        return getLocationPoints()
                .flatMap(locationPoints -> {
                    if (locationPoints == null)
                        locationPoints = new ArrayList<>();
                    if (!locationPoints.contains(locationPoint))
                        locationPoints.add(locationPoint);
                    return saveLocationPoints(locationPoints);
                }).map(m -> locationPoint);
    }

    /**
     * Retrieves a  locationPoints from database
     *
     * @return Observable of a LocationPoints
     */
    public Observable<List<LocationPoint>> getLocationPoints() {
        return RxPaper.book(LOCATION_POINTS_BOOK).read(LOCATION_POINTS_KEY);
    }
}
