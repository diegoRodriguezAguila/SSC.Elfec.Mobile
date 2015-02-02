package com.elfec.ssc.view.gcmservices;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.activeandroid.util.Log;
import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.LocationManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.view.LocationServices;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

public class UpdatePointsGCMHandler implements IGCMHandler {
	private final int NOTIF_ID = 3;
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
			
			LocationManager.registerLocations(points);
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
