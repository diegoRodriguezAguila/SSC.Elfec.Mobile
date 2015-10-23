package com.elfec.ssc.presenter.views;

import java.util.List;

import android.location.Location;

import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.security.PreferencesManager;

public interface ILocationServices {
	public PreferencesManager getPreferences();
	public void showLocationPoints(List<LocationPoint> point);
	public void showLocationServicesErrors(List<Exception> errors);
	public Location getCurrentLocation();
	public void showDetailMessage(String message);
	/**
	 * Obtiene el WSTokenRequester con el contexto de la actividad actual
	 * @return
	 */
	public WSTokenRequester getWSTokenRequester();
	public void informNoInternetConnection();
}
