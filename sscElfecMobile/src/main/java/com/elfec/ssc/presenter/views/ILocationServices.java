package com.elfec.ssc.presenter.views;

import android.location.Location;

import com.elfec.ssc.model.LocationPoint;

import java.util.List;

public interface ILocationServices {
	void showLocationPoints(List<LocationPoint> point);
	void showLocationServicesErrors(List<Exception> errors);
	Location getCurrentLocation();
	void showDetailMessage(String message);
	void informNoInternetConnection();
}
