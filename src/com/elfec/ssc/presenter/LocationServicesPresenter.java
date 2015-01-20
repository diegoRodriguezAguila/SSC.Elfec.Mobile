package com.elfec.ssc.presenter;

import java.util.List;

import android.location.Location;
import android.os.Looper;

import com.elfec.ssc.businesslogic.LocationManager;
import com.elfec.ssc.businesslogic.webservices.LocationPointWS;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.ILocationServices;

public class LocationServicesPresenter {

	private ILocationServices view;
	private List<LocationPoint> points;
	private LocationDistance lastSelectedDistance;
	
	
	public LocationServicesPresenter(ILocationServices view) {
		points=null;
		lastSelectedDistance=LocationDistance.ALL;
		this.view = view;
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
	 * Filtra la lista de puntos según el tipo de punto
	 * @param selectedType
	 */
	public void setSelectedType(LocationPointType selectedType)
	{
		if(selectedType==LocationPointType.ALL)
			points=LocationPoint.getAll(LocationPoint.class);
		else
			points=LocationPoint.getPointsByType(selectedType);
		setSelectedDistance(lastSelectedDistance);
		view.getPreferences().setSelectedLocationPointType(selectedType);
	}
	
	/**
	 * Filtra la lista de puntos según el tipo de proximidad definido, usando el punto de locación exacto
	 * @param distance
	 */
	public void setSelectedDistance(LocationDistance distance, Location currentLocation)
	{
		lastSelectedDistance = distance;
		view.showLocationPoints((distance==LocationDistance.ALL)?
				points:LocationManager.getNearestPoints(points, currentLocation, 1000));
		view.getPreferences().setSelectedLocationPointDistance(distance);
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
	 * Updatea
	 */
	public void updateSelectedDistancePoints(Location recievedLocation)
	{
		setSelectedDistance(lastSelectedDistance, recievedLocation);
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
				if(view.getPreferences().isFirstLoadLocations())
				{
				LocationPointWS pointWS=new LocationPointWS();
				pointWS.getAllLocationPoints(new IWSFinishEvent<List<LocationPoint>>() {
					
					@Override
					public void executeOnFinished(final WSResponse<List<LocationPoint>> result) {
						if(result.getErrors().size()==0)
						{
							LocationManager.RegisterLocations(result.getResult());
							view.getPreferences().setLoadLocationsAlreadyUsed();
							verifyShowLocationPoints(result.getResult());
						}
						else
						{
							view.showLocationServicesErrors(result.getErrors());
						}
					}


				});
			}
			else
			{
				verifyShowLocationPoints(LocationPoint.getAll(LocationPoint.class));
			}
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
