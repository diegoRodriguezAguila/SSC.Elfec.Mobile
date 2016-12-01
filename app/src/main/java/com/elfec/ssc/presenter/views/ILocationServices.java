package com.elfec.ssc.presenter.views;

import android.location.Location;

import com.elfec.ssc.model.LocationPoint;

import java.util.List;

public interface ILocationServices extends ILoadView<List<LocationPoint>> {

    void showLocationServicesErrors(List<Exception> errors);

    Location getCurrentLocation();

    void showDetailMessage(String message);

    void informNoInternetConnection();
}
