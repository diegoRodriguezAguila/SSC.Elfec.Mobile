package com.elfec.ssc.gcmservices;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.activeandroid.util.Log;
import com.elfec.ssc.businesslogic.LocationPointsManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.presenter.LocationServicesPresenter;
import com.elfec.ssc.view.LocationServices;
/**
 * Maneja las notificaciones de recepción de nuevos puntos de ubicación
 * @author drodriguez
 *
 */
public class PointsUpdateGCMHandler implements IGCMHandler {
	//private final int NOTIF_ID = 3;
	@Override
	public void handleGCMessage(Bundle messageInfo,
			NotificationManager notifManager, Builder builder) {
		try
		{
			
			JSONArray result=new JSONArray(messageInfo.getString("points"));
			int length=result.length();
			List<LocationPoint> points=new ArrayList<LocationPoint>();
			for(int i=0;i<length;i++)
			{
				JSONObject point=result.getJSONObject(i);
				points.add(new LocationPoint(point.getString("InstitutionName"), point.getString("Address"), point.getString("Phone"),point.getString("StartAttention"), point.getString("EndAttention"), point.getDouble("Latitude"), point.getDouble("Longitude"),(short) point.getInt("Type")));
			}
			
			LocationPointsManager.registerLocations(points);
			LocationServicesPresenter presenter = ViewPresenterManager
					.getPresenter(LocationServicesPresenter.class);
			if (presenter != null)
			{
				presenter.loadLocations();
			}
		}
		catch(Exception ex)
		{
			Log.i(ex.getMessage());
		}
		
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return LocationServices.class;
	}

}
