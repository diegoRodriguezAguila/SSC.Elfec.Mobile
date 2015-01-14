package com.elfec.ssc.presenter;

import java.util.List;

import com.elfec.ssc.businesslogic.webservices.LocationPointWS;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.ILocationServices;

public class LocationServicesPresenter {

	@SuppressWarnings("unused")
	private ILocationServices view;
	
	public LocationServicesPresenter(ILocationServices view) {
		this.view = view;
	}
	public void loadLocations()
	{
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				LocationPointWS pointWS=new LocationPointWS();
				pointWS.getAllLocationPoints(new IWSFinishEvent<List<LocationPoint>>() {
					
					@Override
					public void executeOnFinished(final WSResponse<List<LocationPoint>> result) {
						if(ThreadMutex.instance("LoadMap").isFree())
							showLocationPoints(result);
						else
							ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(new OnReleaseThread() {
								
								@Override
								public void threadReleased() {
									showLocationPoints(result);
								}
							});
					}


				});
			}
		});
		thread.start();
	}
	private void showLocationPoints(
			WSResponse<List<LocationPoint>> result) {
		List<LocationPoint> points=result.getResult();
		for(LocationPoint point : points)
		{
			view.setPoint(point);
		}
	}
}
