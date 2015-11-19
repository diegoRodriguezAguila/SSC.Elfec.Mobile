package com.elfec.ssc.presenter;

import android.location.Location;
import android.os.Looper;

import com.elfec.ssc.businesslogic.LocationPointsManager;
import com.elfec.ssc.businesslogic.webservices.LocationPointWS;
import com.elfec.ssc.businesslogic.webservices.SscTokenRequester;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.helpers.utils.LocationServicesMessages;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.SscTokenReceivedCallback;
import com.elfec.ssc.model.exceptions.OutdatedAppException;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.ILocationServices;
import com.elfec.ssc.security.AppPreferences;

import java.util.List;

public class LocationServicesPresenter {

    private ILocationServices view;
    private List<LocationPoint> points;
    private LocationPointType lastSelectedType;
    private LocationDistance lastSelectedDistance;
    private Location currentLocation;
    private int distanceRange;
    private SscTokenRequester mSscTokenRequester;
    private final int MIN_DISTANCE_DIFFERENCE = 250;


    public LocationServicesPresenter(ILocationServices view) {
        points = null;
        lastSelectedDistance = LocationDistance.ALL;
        lastSelectedType = LocationPointType.BOTH;
        this.view = view;
        distanceRange = AppPreferences.instance().getConfiguredDistance();
        mSscTokenRequester = new SscTokenRequester(AppPreferences.getApplicationContext());
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
            points = LocationPoint.getAll(LocationPoint.class);
        else
            points = LocationPoint.getPointsByType(selectedType);
        setSelectedDistance(lastSelectedDistance);
        AppPreferences.instance().setSelectedLocationPointType(selectedType);
        view.showDetailMessage(LocationServicesMessages.buildMessage(selectedType, lastSelectedDistance));
    }

    /**
     * Filtra la lista de puntos segón el tipo de proximidad definido, usando el punto de locación exacto
     *
     * @param distance distancia
     */
    public void setSelectedDistance(LocationDistance distance, Location currentLocation) {
        lastSelectedDistance = distance;
        view.showLocationPoints((distance == LocationDistance.ALL) ?
                points : LocationPointsManager.getNearestPoints(points, currentLocation, distanceRange));
        AppPreferences.instance().setSelectedLocationPointDistance(distance);
        view.showDetailMessage(LocationServicesMessages.buildMessage(lastSelectedType, distance));
    }

    /**
     * Filtra la lista de puntos segón el tipo de proximidad definido, obteniendo la locación de la vista
     *
     * @param distance distancia
     */
    public void setSelectedDistance(LocationDistance distance) {
        setSelectedDistance(distance, view.getCurrentLocation() == null ? (new Location("gps")) : view.getCurrentLocation());
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
     * Obtiene la lista de puntos de ubicación en un hilo
     */
    public void loadLocations() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                mSscTokenRequester.getTokenAsync(new SscTokenReceivedCallback() {
                    @Override
                    public void onSscTokenReceived(WSResponse<SscToken> wsTokenResult) {
                        new LocationPointWS(wsTokenResult.getResult())
                                .getAllLocationPoints(new IWSFinishEvent<List<LocationPoint>>() {
                                    @Override
                                    public void executeOnFinished(final WSResponse<List<LocationPoint>> result) {
                                        if (result.getErrors().size() == 0) {
                                            LocationPointsManager.removeLocations(result.getResult());
                                            LocationPointsManager.registerLocations(result.getResult());
                                        } else {
                                            if (isOutdatedApp(result.getErrors()))
                                                view.showLocationServicesErrors(result.getErrors());
                                            else view.informNoInternetConnection();
                                        }
                                        verifyShowLocationPoints();
                                    }
                                });
                    }
                });
                Looper.loop();
            }
        }).start();
    }

    /**
     * Verifica si los recursos de google maps cargaron para mostrar los puntos de ubicacion
     *
     */
    private void verifyShowLocationPoints() {
        ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(new OnReleaseThread() {
            @Override
            public void threadReleased() {
                lastSelectedDistance = AppPreferences.instance().getSelectedLocationPointDistance();
                setSelectedType(AppPreferences.instance().getSelectedLocationPointType());
            }
        });
    }

    /**
     * Verifica si alguno de los errores contiene el error de versión
     * de aplicación no actualizada
     * @param errors errores
     * @return true si es que contiene ese tipo de error
     */
    private boolean isOutdatedApp(List<Exception> errors){
        for(Exception e : errors){
            if(e instanceof OutdatedAppException)
                return true;
        }
        return false;
    }

}
