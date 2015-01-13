package com.elfec.ssc.presenter;

import com.elfec.ssc.presenter.views.ILocationServices;

public class LocationServicesPresenter {

	@SuppressWarnings("unused")
	private ILocationServices view;
	
	public LocationServicesPresenter(ILocationServices view) {
		this.view = view;
	}
}
