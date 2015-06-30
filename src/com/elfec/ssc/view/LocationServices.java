package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.presenter.LocationServicesPresenter;
import com.elfec.ssc.presenter.views.ILocationServices;
import com.elfec.ssc.security.PreferencesManager;
import com.elfec.ssc.view.adapters.MarkerPopupAdapter;
import com.elfec.ssc.view.controls.GMapFragment;
import com.elfec.ssc.view.controls.SetupDistanceDialogService;
import com.elfec.ssc.view.controls.events.OnDistanceSetup;
import com.elfec.ssc.view.controls.events.OnMapReadyCallback;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LocationServices extends AppCompatActivity implements ILocationServices, OnMapReadyCallback {

	private final double LAT_ELFEC = -17.3934795;
	private final double LNG_ELFEC = -66.1651093;
	private final float DEFAULT_ZOOM = 13.5f;
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	private AlertDialog waitingMapDialog;
	
	private LocationServicesPresenter presenter;
	
	private Toolbar toolbar;
	private GoogleMap map;
	private MenuItem menuItemSetupDistance;
	private Marker lastOpenedMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_services);
		showWaitingDialog();
		presenter = new LocationServicesPresenter(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar); 
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(R.string.location_services_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		setupMap();
		ThreadMutex.instance("LoadMap").setBusy();
		presenter.loadLocations();
		setSelectedOptions();
		croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(getResources().getColor(R.color.ssc_elfec_color_highlight)).build();
	}
	protected void onResume()
	{
		super.onResume();
		ViewPresenterManager.setPresenter(presenter);
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		ViewPresenterManager.setPresenter(null);
	}
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_services, menu);
		menuItemSetupDistance = menu.findItem(R.id.setup_distance);
		if(getPreferences().getSelectedLocationPointDistance()!=LocationDistance.NEAREST)
			menuItemSetupDistance.setVisible(false);
		return true;
	}
	
	
	@Override
	public void onBackPressed() {
		if(lastOpenedMarker!=null && lastOpenedMarker.isInfoWindowShown())
		{
			lastOpenedMarker.hideInfoWindow();
			lastOpenedMarker = null;
		}
		else
		{
		    finish();//go back to the previous Activity
		    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
		}
	}
	
	@Override
	public WSTokenRequester getWSTokenRequester() {
		return new WSTokenRequester(this);
	}
	
	/**
	 * Muestra un diálogo de espera mientras carga el mapa
	 */
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
	private void setupMap() {
		 new Handler().postDelayed(new Runnable() {
		       @Override
		        public void run() {		    
		            if (!isFinishing()) {
		            	GoogleMapOptions options = new GoogleMapOptions();
		            	options.rotateGesturesEnabled(false)
		            	.camera(new CameraPosition(new LatLng(LAT_ELFEC,LNG_ELFEC), DEFAULT_ZOOM, 0, 0))
		                .tiltGesturesEnabled(false).zoomControlsEnabled(false);
		                FragmentManager fm = getSupportFragmentManager();
		                GMapFragment mapFragment = new GMapFragment(LocationServices.this, options);
	    				waitingMapDialog.dismiss();
	    				System.gc();
		                fm.beginTransaction().add(R.id.google_map_container, mapFragment).commit();
		            }
		        }
		    }, 500);
	}
	
	/**
	 * Inicializa los adapters y distintas funciones del mapa, debe llamarse
	 * cuando el mapa ya se ha cargado
	 */
	private void initializeMapOptions() {
		ThreadMutex.instance("LoadMap").setFree();
		map.setInfoWindowAdapter(new MarkerPopupAdapter(getLayoutInflater()));
		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {			
			@Override
			public void onMyLocationChange(Location recievedLocation) {
				presenter.updateSelectedDistancePoints(recievedLocation);
			}
		});
		map.setOnMarkerClickListener(new OnMarkerClickListener() {			
			@Override
			public boolean onMarkerClick(Marker marker) {
				lastOpenedMarker = marker;
				return false;
			}
		});
	}
	
//#region Interface Methods
	@Override
	public void showLocationPoints(final List<LocationPoint> points) {
		ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(new OnReleaseThread() {
			
			@Override
			public void threadReleased() {
				runOnUiThread(new Runnable() {	
					@Override
					public void run() {	
							map.clear();
							for(LocationPoint point : points)
							{
								addMarker(point);
							}						
					}
				});
			}
		});
		
	}
	@Override
	public PreferencesManager getPreferences() {
		return new PreferencesManager(getApplicationContext());
	}

	@Override
	public void showLocationServicesErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				StringBuilder msg = new StringBuilder();
				int size = errors.size();
				if(size==1)
					msg.append(errors.get(0).getMessage()).toString();
				else
				{
					for (int i = 0; i < size; i++) {
						msg.append("● ").append(errors.get(i).getMessage());
						msg.append((i<size-1?"\n":""));
					}
				}
				AlertDialogPro.Builder builder = new AlertDialogPro.Builder(LocationServices.this);
				builder.setTitle(R.string.errors_on_get_points_title)
				.setMessage(msg)
				.setPositiveButton(R.string.btn_ok, null)
				.show();
			}
		});
	}
	

	@Override
	public Location getCurrentLocation() {
		try
		{
			return map.getMyLocation();
		}
		catch(IllegalStateException e)
		{
			return new Location("gps");
		}
	}
	
	@Override
	public void showDetailMessage(String message) {
		Crouton.clearCroutonsForActivity(this);
		Crouton.makeText(this, message, croutonStyle, R.id.view_content).show();
	}
	
	//#endregion
	
	/**
	 * Añade un marker al mapa
	 * @param point
	 */
	private void addMarker(LocationPoint point) {
		map.addMarker(new MarkerOptions()
				.position(
						new LatLng(point.getLatitude(), point
								.getLongitude()))
				.title(point.getType().toString() + "\n"+point.getInstitutionName())
				.snippet(point.getAddress() + "\n" + point.getPhone()
						+ ((point.getStartAttention() != null) ? ("\n"+point.getStartAttention()+" - "+point.getEndAttention()):""))
				.icon(BitmapDescriptorFactory.fromResource(point
						.getType() == LocationPointType.OFFICE ? R.drawable.office_marker
						: R.drawable.paypoint_marker)));
	}
	
	public void rbtnShowOfficesClick(View view)
	{
		if(((RadioButton) view).isChecked())
			presenter.setSelectedType(LocationPointType.OFFICE);
		
	}
	
	public void rbtnShowPayPointsClick(View view)
	{
		if(((RadioButton) view).isChecked())
			presenter.setSelectedType(LocationPointType.PAYPOINT);
	}
	
	public void rbtnShowAllClick(View view)
	{
		if(((RadioButton) view).isChecked())
			presenter.setSelectedType(LocationPointType.BOTH);
	}
	
	public void rbtnShowByDistanceAllClick(View view)
	{
		if(((RadioButton) view).isChecked())
		{
			menuItemSetupDistance.setVisible(false);
			presenter.setSelectedDistance(LocationDistance.ALL);
		}
	}
	
	public void rbtnShowNearestClick(View view)
	{
		if(((RadioButton) view).isChecked())
		{
			menuItemSetupDistance.setVisible(true);
			presenter.setSelectedDistance(LocationDistance.NEAREST);
		}
	}
	
	/**
	 * Obtiene las preferencias guardadas de las opciones seleccionadas
	 */
	private void setSelectedOptions() {
		setCheckedLocationType();
		setCheckedLocationDistance();
	}
	
	/**
	 * Marca la opción seleccionada por ultima vez que se tenga guardada en las preferencias
	 * en caso de no existir se muestran todos los tipos por defecto
	 */
	private void setCheckedLocationType()
	{
		RadioGroup mapShowType = ((RadioGroup)findViewById(R.id.map_show_type));
		LocationPointType selectedType = getPreferences().getSelectedLocationPointType();
		switch(selectedType)
		{
			case OFFICE:
			{
				mapShowType.check(R.id.rbtn_show_offices);
				break;
			}
			case PAYPOINT:
			{
				mapShowType.check(R.id.rbtn_show_paypoints);
				break;
			}
			default:
			{
				mapShowType.check(R.id.rbtn_show_both);
				break;
			}
		}
	}
	/**
	 * Marca la opción seleccionada por ultima vez que se tenga guardada en las preferencias
	 * en caso de no existir se muestran todos los tipos por defecto
	 */
	private void setCheckedLocationDistance() {
		LocationDistance selectedDistance = getPreferences().getSelectedLocationPointDistance();
		RadioGroup mapShowDistance = ((RadioGroup)findViewById(R.id.map_show_distance));
		switch(selectedDistance)
		{
			case NEAREST:
			{
				mapShowDistance.check(R.id.rbtn_show_nearest);
				break;
			}
			default:
			{
				mapShowDistance.check(R.id.rbtn_show_all);
				break;
			}
		}
	}
	
	public void menuItemSetupDistanceClick(MenuItem item)
	{
		SetupDistanceDialogService.instanceService(this, new OnDistanceSetup() {			
			@Override
			public void onDistanceSelected(int selectedDistance) {
				presenter.setDistanceRange(selectedDistance);
			}
		}).show();
	}
	
	@Override
	public void onMapReady(GoogleMap obtainedMap) {
		map = obtainedMap;
		initializeMapOptions();
	}
	@Override
	public void informNoInternetConnection() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SuperToast.create(LocationServices.this, R.string.no_internet_msg, SuperToast.Duration.SHORT, 
					    Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
			}
		});
	}
}
