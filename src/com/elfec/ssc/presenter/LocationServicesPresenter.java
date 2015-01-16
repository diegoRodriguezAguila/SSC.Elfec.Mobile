package com.elfec.ssc.presenter;

import java.util.List;

import android.os.Looper;
import com.elfec.ssc.businesslogic.LocationManager;
import com.elfec.ssc.businesslogic.webservices.LocationPointWS;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.ILocationServices;

public class LocationServicesPresenter {

	private ILocationServices view;
	private List<LocationPoint> points;
	public List<LocationPoint> getPoints()
	{
		return points;
	}
	public LocationServicesPresenter(ILocationServices view) {
		points=null;
		this.view = view;
	}
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
		/**
		 * Verifica si los recursos de google maps cargaron para mostrar los puntos de ubicacion
		 * @param points
		 */
			private void verifyShowLocationPoints(
					final List<LocationPoint> points) {
					ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(new OnReleaseThread() {						
						@Override
						public void threadReleased() {
							showLocationPoints(points);
						}
					});
			}
		});
		thread.start();
	}
	/**
	 * Muestra los puntos en el mapa
	 * @param result
	 */
	private void showLocationPoints(
			List<LocationPoint> result) {
		points=result;
		for(LocationPoint point : points)
		{
			view.setPoint(point);
		}
	}
}
