package com.elfec.ssc.presenter.views;

import java.util.List;

import android.location.Location;

import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.model.LocationPoint;

public interface ILocationServices {
	public PreferencesManager getPreferences();
	public void showLocationPoints(List<LocationPoint> point);
	public void showLocationServicesErrors(List<Exception> errors);
	public Location getCurrentLocation();
	public void showDetailMessage(String message);
}
