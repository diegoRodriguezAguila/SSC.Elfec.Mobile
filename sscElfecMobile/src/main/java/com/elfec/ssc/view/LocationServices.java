package com.elfec.ssc.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.presenter.LocationServicesPresenter;
import com.elfec.ssc.presenter.views.ILocationServices;
import com.elfec.ssc.security.PreferencesManager;
import com.elfec.ssc.view.adapters.MarkerPopupAdapter;
import com.elfec.ssc.view.controls.GMapFragment;
import com.elfec.ssc.view.controls.ProgressDialogService;
import com.elfec.ssc.view.controls.SetupDistanceDialogService;
import com.elfec.ssc.view.controls.events.OnDistanceSetup;
import com.elfec.ssc.view.controls.events.OnMapReadyCallback;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LocationServices extends AppCompatActivity implements
		ILocationServices, OnMapReadyCallback {

	private static final double LAT_ELFEC = -17.3934795;
	private static final double LNG_ELFEC = -66.1651093;
	private static final float DEFAULT_ZOOM = 15.5f;
	private final Handler mHandler = new Handler();
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	private ProgressDialogService waitingMapDialog;

	private LocationServicesPresenter presenter;

	private GoogleMap map;
	private MenuItem menuItemSetupDistance;
	private Marker lastOpenedMarker;
	private BitmapDescriptor mBitmapOffice;
	private BitmapDescriptor mBitmapPayPoint;
	protected @Bind(R.id.map_show_distance) RadioGroup mMapShowDistance;
	protected @Bind(R.id.map_show_type) RadioGroup mMapShowType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_services);
		showWaitingDialog();
		presenter = new LocationServicesPresenter(this);
		ButterKnife.bind(this);
		setupMap();
		ThreadMutex.instance("LoadMap").setBusy();
		presenter.loadLocations();
		setSelectedOptions();
		croutonStyle = new de.keyboardsurfer.android.widget.crouton.Style.Builder()
				.setFontName("fonts/segoe_ui_semilight.ttf")
				.setTextSize(16)
				.setBackgroundColorValue(
						ContextCompat.getColor(this,
								R.color.ssc_elfec_color_highlight)).build();
	}

	protected void onResume() {
		super.onResume();
		ViewPresenterManager.setPresenter(presenter);
	}

	@Override
	protected void onPause() {
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
		if (getPreferences().getSelectedLocationPointDistance() != LocationDistance.NEAREST)
			menuItemSetupDistance.setVisible(false);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (lastOpenedMarker != null && lastOpenedMarker.isInfoWindowShown()) {
			lastOpenedMarker.hideInfoWindow();
			lastOpenedMarker = null;
		} else {
			finish();// go back to the previous Activity
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_right_out);
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
		waitingMapDialog = new ProgressDialogService(this);
		waitingMapDialog.setMessage(this.getResources().getString(
				R.string.waiting_map_msg));
		waitingMapDialog.setCancelable(false);
		waitingMapDialog.setCanceledOnTouchOutside(false);
		waitingMapDialog.show();
	}

	/**
	 * Asigna el nivel de zoom y centrea el mapa en la ciudad de cochabamba a
	 * elfec como centro
	 */
	private void setupMap() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isFinishing()) {
					GoogleMapOptions options = new GoogleMapOptions();
					options.rotateGesturesEnabled(false)
							.camera(new CameraPosition(new LatLng(LAT_ELFEC,
									LNG_ELFEC), DEFAULT_ZOOM, 0, 0))
							.tiltGesturesEnabled(false)
							.zoomControlsEnabled(false);
					FragmentManager fm = getSupportFragmentManager();
					GMapFragment mapFragment = new GMapFragment().setOnMapReadyCallback(
							LocationServices.this).setGoogleMapOptions(options);
					waitingMapDialog.dismiss();
					System.gc();
					fm.beginTransaction()
							.add(R.id.google_map_container, mapFragment)
							.commit();
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

	/**
	 * Crea un hilo para poner los puntos en el mapa, los pone de 10 en 10
	 * utilizando el hilo principal, realizando llamadas a {@link #putPointsInMapUI}
	 * @param points lista de puntos
	 */
	private void putPointsInMapAsync(final List<LocationPoint> points) {
		final int range = 10;
		final int delay = 70;
		new Thread(new Runnable() {
			@Override
			public void run() {
				int size = points.size();
				int iterations = (size/range)+1;
				int topRange;
				for(int i=0; i< iterations; i++){
					topRange = range*(i+1);
					putPointsInMapUI(
							points.subList(range*i, topRange<size? topRange : size)
							, i*delay);
				}
			}
		}).start();
	}

	/**
	 * Pone los puntos en el mapa, en el hilo de la UI, esta operación podría bloquear
	 * el hilo principal si son demasiados puntos, para listas grandes utilice {@link #putPointsInMapAsync}
	 * @param points lista de puntos
	 * @param delay el tiempo delay en el que se pondrá en la UI los puntos
	 */
	private void putPointsInMapUI(final List<LocationPoint> points, int delay) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for (LocationPoint point : points) {
					addMarker(point);
				}
			}
		}, delay);
	}


	// #region Interface Methods
	@Override
	public void showLocationPoints(final List<LocationPoint> points) {
		ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(
				new OnReleaseThread() {
					@Override
					public void threadReleased() {
						map.clear();
						mBitmapOffice = BitmapDescriptorFactory.fromResource(R.drawable.office_marker);
						mBitmapPayPoint = BitmapDescriptorFactory.fromResource(R.drawable.paypoint_marker);
						putPointsInMapAsync(points);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(
						LocationServices.this);
				builder.setTitle(R.string.errors_on_get_points_title)
						.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
						.setPositiveButton(R.string.btn_ok, null).show();
			}
		});
	}

	@Override
	public Location getCurrentLocation() {
		try {
			return map.getMyLocation();
		} catch (IllegalStateException e) {
			return new Location("gps");
		}
	}

	@Override
	public void showDetailMessage(String message) {
		Crouton.clearCroutonsForActivity(this);
		Crouton.makeText(this, message, croutonStyle, R.id.view_content).show();
	}

	// #endregion

	/**
	 * Añade un marker al mapa
	 *
	 * @param point point
	 */
	private void addMarker(LocationPoint point) {
		map.addMarker(new MarkerOptions()
				.position(new LatLng(point.getLatitude(), point.getLongitude()))
				.title(point.getType().toString() + "\n"
						+ point.getInstitutionName())
				.snippet(
						point.getAddress()
								+ "\n"
								+ point.getPhone()
								+ ((point.getStartAttention() != null && !point.getStartAttention().equals("")) ? ("\n"
								+ point.getStartAttention() + " - " + point
								.getEndAttention()) : ""))
				.icon(point.getType() == LocationPointType.OFFICE? mBitmapOffice:mBitmapPayPoint));
	}

	public void rbtnShowOfficesClick(View view) {
		if (((RadioButton) view).isChecked())
			presenter.setSelectedType(LocationPointType.OFFICE);

	}

	public void rbtnShowPayPointsClick(View view) {
		if (((RadioButton) view).isChecked())
			presenter.setSelectedType(LocationPointType.PAYPOINT);
	}

	public void rbtnShowAllClick(View view) {
		if (((RadioButton) view).isChecked())
			presenter.setSelectedType(LocationPointType.BOTH);
	}

	public void rbtnShowByDistanceAllClick(View view) {
		if (((RadioButton) view).isChecked()) {
			menuItemSetupDistance.setVisible(false);
			presenter.setSelectedDistance(LocationDistance.ALL);
		}
	}

	public void rbtnShowNearestClick(View view) {
		if (((RadioButton) view).isChecked()) {
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
	 * Marca la opción seleccionada por ultima vez que se tenga guardada en las
	 * preferencias en caso de no existir se muestran todos los tipos por
	 * defecto
	 */
	private void setCheckedLocationType() {
		LocationPointType selectedType = getPreferences()
				.getSelectedLocationPointType();
		switch (selectedType) {
			case OFFICE: {
				mMapShowType.check(R.id.rbtn_show_offices);
				break;
			}
			case PAYPOINT: {
				mMapShowType.check(R.id.rbtn_show_paypoints);
				break;
			}
			default: {
				mMapShowType.check(R.id.rbtn_show_both);
				break;
			}
		}
	}

	/**
	 * Marca la opción seleccionada por ultima vez que se tenga guardada en las
	 * preferencias en caso de no existir se muestran todos los tipos por
	 * defecto
	 */
	private void setCheckedLocationDistance() {
		LocationDistance selectedDistance = getPreferences()
				.getSelectedLocationPointDistance();
		switch (selectedDistance) {
			case NEAREST: {
				mMapShowDistance.check(R.id.rbtn_show_nearest);
				break;
			}
			default: {
				mMapShowDistance.check(R.id.rbtn_show_all);
				break;
			}
		}
	}

	public void menuItemSetupDistanceClick(MenuItem item) {
		if (ButtonClicksHelper.canClickButton()) {
			SetupDistanceDialogService.instanceService(this,
					new OnDistanceSetup() {
						@Override
						public void onDistanceSelected(int selectedDistance) {
							presenter.setDistanceRange(selectedDistance);
						}
					}).show();
		}
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
				SuperToast.create(LocationServices.this,
						R.string.no_internet_msg, SuperToast.Duration.SHORT,
						Style.getStyle(Style.BLUE, SuperToast.Animations.FADE))
						.show();
			}
		});
	}
}
