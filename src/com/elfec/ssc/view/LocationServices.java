package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.alertdialogpro.ProgressDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.presenter.LocationServicesPresenter;
import com.elfec.ssc.presenter.views.ILocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class LocationServices extends ActionBarActivity implements ILocationServices {

	private final double LAT_ELFEC = -17.3934795;
	private final double LNG_ELFEC = -66.1651093;
	private final float DEFAULT_ZOOM = 13.5f;
	private AlertDialog waitingMapDialog;
	
	@SuppressWarnings("unused")
	private LocationServicesPresenter presenter;
	@SuppressWarnings("unused")
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_services);
		presenter = new LocationServicesPresenter(this);
		showWaitingDialog();
		setDefaultZoomView();
	}

	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_services, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
	

	private void showWaitingDialog() {
		waitingMapDialog = new ProgressDialogPro(this, R.style.Theme_FlavoredMaterialLight);
		waitingMapDialog.setMessage(this.getResources().getString(R.string.waiting_map_msg));
		waitingMapDialog.setCancelable(false);
		waitingMapDialog.setCanceledOnTouchOutside(false);
		waitingMapDialog.show();
	}

	/**
	 * Asigna el nivel de zoom y centrea el mapa en la ciudad de cochabamba a elfec como centro
	 */
	private void setDefaultZoomView() {
		 new Handler().postDelayed(new Runnable() {
		        @Override
		        public void run() {		        	
		            if (!isFinishing()) {
		            	GoogleMapOptions options = new GoogleMapOptions();
		            	options.rotateGesturesEnabled(false)
		            	.camera(new CameraPosition(new LatLng(LAT_ELFEC,LNG_ELFEC), DEFAULT_ZOOM, 0, 0))
		                .tiltGesturesEnabled(false);
		                FragmentManager fm = getSupportFragmentManager();
		                SupportMapFragment mapFragment = SupportMapFragment
		                        .newInstance(options);
        				waitingMapDialog.dismiss();
		                fm.beginTransaction().replace(R.id.google_map_container, mapFragment).commit();
		                mapFragment.getMapAsync(
		        				new OnMapReadyCallback() {					
		        			@Override
		        			public void onMapReady(GoogleMap obtainedMap) {
		        				map = obtainedMap;

		        			}
		        		});
		            }
		        }
		    }, 500);
	}
	
}
