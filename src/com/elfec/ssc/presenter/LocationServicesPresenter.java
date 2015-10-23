package com.elfec.ssc.presenter;

import java.util.List;

import android.location.Location;
import android.os.Looper;

import com.elfec.ssc.businesslogic.LocationPointsManager;
import com.elfec.ssc.businesslogic.webservices.LocationPointWS;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.helpers.utils.LocationServicesMessages;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.WSTokenReceivedCallback;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.ILocationServices;

public class LocationServicesPresenter {

	private ILocationServices view;
	private List<LocationPoint> points;
	private LocationPointType lastSelectedType;
	private LocationDistance lastSelectedDistance;
	private Location currentLocation;
	private int distanceRange;
	private final int MIN_DISTANCE_DIFFERENCE = 250;
	
	
	public LocationServicesPresenter(ILocationServices view) {
		points=null;
		lastSelectedDistance=LocationDistance.ALL;
		lastSelectedType=LocationPointType.BOTH;
		this.view = view;
		distanceRange=view.getPreferences().getConfiguredDistance();
	}
	
	/**
	 * Obtiene la lista de puntos
	 * @return
	 */
	public List<LocationPoint> getPoints()
	{
		return points;
	}
	
	/**
	 * Asigna la distancia en metros para realizar la filtración de puntos, cuando la opción
	 * de más cercanos es seleccionada
	 * @param distance
	 */
	public void setDistanceRange(int distance)
	{
		if(distance != distanceRange)
		{
			distanceRange = distance;
			if(lastSelectedDistance==LocationDistance.NEAREST)
			{
				setSelectedDistance(lastSelectedDistance);
			}
		}
	}
	
	/**
	 * Filtra la lista de puntos según el tipo de punto
	 * @param selectedType
	 */
	public void setSelectedType(LocationPointType selectedType)
	{
		lastSelectedType = selectedType;
		if(selectedType==LocationPointType.BOTH)
			points=LocationPoint.getAll(LocationPoint.class);
		else
			points=LocationPoint.getPointsByType(selectedType);
		setSelectedDistance(lastSelectedDistance);
		view.getPreferences().setSelectedLocationPointType(selectedType);
		view.showDetailMessage(LocationServicesMessages.buildMessage(selectedType, lastSelectedDistance));
	}
	
	/**
	 * Filtra la lista de puntos según el tipo de proximidad definido, usando el punto de locación exacto
	 * @param distance
	 */
	public void setSelectedDistance(LocationDistance distance, Location currentLocation)
	{
		lastSelectedDistance = distance;
		view.showLocationPoints((distance==LocationDistance.ALL)?
				points:LocationPointsManager.getNearestPoints(points, currentLocation, distanceRange));
		view.getPreferences().setSelectedLocationPointDistance(distance);
		view.showDetailMessage(LocationServicesMessages.buildMessage(lastSelectedType, distance));
	}
	
	/**
	 * Filtra la lista de puntos según el tipo de proximidad definido, obteniendo la locación de la vista
	 * @param distance
	 */
	public void setSelectedDistance(LocationDistance distance)
	{
		setSelectedDistance(distance, view.getCurrentLocation()==null?(new Location("gps")):view.getCurrentLocation());
	}
	
	/**
	 * Actualiza el filtro de ubicación si cuando la ubicación cambia o se obtiene una nueva ubicación
	 */
	public void updateSelectedDistancePoints(Location recievedLocation)
	{
		if(lastSelectedDistance==LocationDistance.NEAREST && 
				(currentLocation==null || (recievedLocation.distanceTo(currentLocation)>MIN_DISTANCE_DIFFERENCE)))
		{
			currentLocation = recievedLocation;
			setSelectedDistance(lastSelectedDistance, recievedLocation);
		}
	}
	
	/**
	 * Obtiene la lista de puntos de ubicación en un hilo
	 */
	public void loadLocations()
	{
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				Looper.prepare();
				view.getWSTokenRequester().getTokenAsync(new WSTokenReceivedCallback() {					
					@Override
					public void onWSTokenReceived(WSResponse<WSToken> wsTokenResult) {
						new LocationPointWS(wsTokenResult.getResult())
							.getAllLocationPoints(new IWSFinishEvent<List<LocationPoint>>() {
							@Override
							public void executeOnFinished(final WSResponse<List<LocationPoint>> result) {
								if(result.getErrors().size()==0)
								{
									LocationPointsManager.registerLocations(result.getResult());
									LocationPointsManager.removeLocations(result.getResult());
									verifyShowLocationPoints(result.getResult());
								}
								else {
									verifyShowLocationPoints(LocationPoint.getAll(LocationPoint.class));
									view.informNoInternetConnection();
								}
							}
						});
					}
				});
			Looper.loop();
			}
		});
		thread.start();
	}
	
	/**
	 * Verifica si los recursos de google maps cargaron para mostrar los puntos de ubicacion
	 * @param points
	 */
	private void verifyShowLocationPoints(final List<LocationPoint> result) {
			ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(new OnReleaseThread() {						
				@Override
				public void threadReleased() {
					points=result;
					lastSelectedDistance = view.getPreferences().getSelectedLocationPointDistance();
					setSelectedType(view.getPreferences().getSelectedLocationPointType());
				}
			});
	}
	
}
