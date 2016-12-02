package com.elfec.ssc.presenter;

import android.location.Location;

import com.elfec.ssc.business_logic.LocationPointsManager;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.helpers.utils.LocationServicesMessages;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.presenter.views.ILocationServices;
import com.elfec.ssc.security.AppPreferences;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LocationServicesPresenter extends BasePresenter<ILocationServices> {

    private List<LocationPoint> allPoints;
    private List<LocationPoint> points;
    private LocationPointType lastSelectedType;
    private LocationDistance lastSelectedDistance;
    private Location currentLocation;
    private int distanceRange;
    private static final int MIN_DISTANCE_DIFFERENCE = 250;


    public LocationServicesPresenter(ILocationServices mView) {
        super(mView);
        points = null;
        lastSelectedDistance = LocationDistance.ALL;
        lastSelectedType = LocationPointType.BOTH;
        distanceRange = AppPreferences.instance().getConfiguredDistance();
    }

    /**
     * Asigna la distancia en metros para realizar la filtración de puntos, cuando la opción
     * de más cercanos es seleccionada
     *
     * @param distance distancia
     */
    public void setDistanceRange(int distance) {
        if (distance != distanceRange) {
            distanceRange = distance;
            if (lastSelectedDistance == LocationDistance.NEAREST) {
                setSelectedDistance(lastSelectedDistance);
            }
        }
    }

    /**
     * Filtra la lista de puntos según el tipo de punto
     *
     * @param selectedType tipo
     */
    public void setSelectedType(LocationPointType selectedType) {
        lastSelectedType = selectedType;
        if (selectedType == LocationPointType.BOTH)
            points = allPoints;
        else
            points = LocationPointsManager.filterByType(allPoints, selectedType);
        setSelectedDistance(lastSelectedDistance);
        AppPreferences.instance().setSelectedLocationPointType(selectedType);
        mView.showDetailMessage(LocationServicesMessages.buildMessage(selectedType, lastSelectedDistance));
    }

    /**
     * Filtra la lista de puntos segón el tipo de proximidad definido, usando el punto de locación exacto
     *
     * @param distance distancia
     */
    public void setSelectedDistance(LocationDistance distance, Location currentLocation) {
        lastSelectedDistance = distance;
        mView.onLoaded((distance == LocationDistance.ALL) ?
                points : LocationPointsManager.getNearestPoints(points, currentLocation, distanceRange));
        AppPreferences.instance().setSelectedLocationPointDistance(distance);
        mView.showDetailMessage(LocationServicesMessages.buildMessage(lastSelectedType, distance));
    }

    /**
     * Filtra la lista de puntos segón el tipo de proximidad definido, obteniendo la locación de la vista
     *
     * @param distance distancia
     */
    public void setSelectedDistance(LocationDistance distance) {
        setSelectedDistance(distance, mView.getCurrentLocation() == null ? (new Location("gps")) :
                mView.getCurrentLocation());
    }

    /**
     * Actualiza el filtro de ubicación si cuando la ubicación cambia o se obtiene una nueva ubicación
     */
    public void updateSelectedDistancePoints(Location recievedLocation) {
        if (lastSelectedDistance == LocationDistance.NEAREST &&
                (currentLocation == null || (recievedLocation.distanceTo(currentLocation) > MIN_DISTANCE_DIFFERENCE))) {
            currentLocation = recievedLocation;
            setSelectedDistance(lastSelectedDistance, recievedLocation);
        }
    }

    /**
     * Obtiene la lista de puntos de ubicación
     */
    public void loadLocationPoints() {
        LocationPointsManager.syncLocationPoints()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(locationPoints -> {
                    allPoints = locationPoints;
                    verifyShowLocationPoints();
                }, mView::onError);
    }

    /**
     * Verifica si los recursos de google maps cargaron para mostrar los puntos de ubicacion
     */
    private void verifyShowLocationPoints() {
        ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(() -> {
            lastSelectedDistance = AppPreferences.instance().getSelectedLocationPointDistance();
            setSelectedType(AppPreferences.instance().getSelectedLocationPointType());
        });
    }
}
