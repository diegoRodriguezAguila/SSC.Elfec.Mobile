package com.elfec.ssc.view.controls;

import com.elfec.ssc.view.controls.events.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

/**
 * Fragmento que se extiende de SupportMapFragment de google play services for froyo
 * aumenta un evento que se llama cuando se carga el mapa
 * @author Diego
 *
 */
public class GMapFragment extends SupportMapFragment {

	private OnMapReadyCallback onMapReadyCallback;
	private GoogleMapOptions options;
	
	public GMapFragment(OnMapReadyCallback onMapReadyCallback)
	{
		super();
		this.onMapReadyCallback = onMapReadyCallback;
	}
	
	public GMapFragment(OnMapReadyCallback onMapReadyCallback, GoogleMapOptions options)
	{
		super();
		this.onMapReadyCallback = onMapReadyCallback;
		this.options = options;
	}
	@Override
	public void onResume()
	{
		super.onResume();
		GoogleMap map = this.getMap();
		if(options!=null)
		{
			map.setMapType(options.getMapType());
			UiSettings uiSettings = map.getUiSettings();
			uiSettings.setCompassEnabled(options.getCompassEnabled()!=null?options.getCompassEnabled():false);
			uiSettings.setRotateGesturesEnabled(options.getRotateGesturesEnabled()!=null?options.getRotateGesturesEnabled():true);
			uiSettings.setTiltGesturesEnabled(options.getTiltGesturesEnabled()!=null?options.getTiltGesturesEnabled():true);
			uiSettings.setZoomControlsEnabled(options.getZoomControlsEnabled()!=null?options.getZoomControlsEnabled():false);
			uiSettings.setZoomGesturesEnabled(options.getZoomGesturesEnabled()!=null?options.getZoomGesturesEnabled():true);
			uiSettings.setScrollGesturesEnabled(options.getScrollGesturesEnabled()!=null?options.getScrollGesturesEnabled():true);
			if(options.getCamera()!=null)
				map.moveCamera(CameraUpdateFactory.newCameraPosition(options.getCamera()));
		}
		if(onMapReadyCallback!=null)
			onMapReadyCallback.onMapReady(map);
	}
	
}
