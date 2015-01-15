package com.elfec.ssc.presenter.views;

import java.util.List;

import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.model.LocationPoint;

public interface ILocationServices {
	public PreferencesManager getPreferences();
	public void setPoint(LocationPoint point);
	public void showLocationServicesErrors(List<Exception> errors);
}
