package com.elfec.ssc.view;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.HtmlCompat;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.enums.LocationDistance;
import com.elfec.ssc.model.enums.LocationPointType;
import com.elfec.ssc.model.exceptions.OutdatedAppException;
import com.elfec.ssc.presenter.LocationServicesPresenter;
import com.elfec.ssc.presenter.views.ILocationServices;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.view.adapters.MarkerPopupAdapter;
import com.elfec.ssc.view.controls.ProgressDialogService;
import com.elfec.ssc.view.controls.SetupDistanceDialogService;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@RuntimePermissions
public class LocationServicesActivity extends AppCompatActivity implements
        ILocationServices, OnMapReadyCallback {

    public static final String TAG = "LocationServices";
    private static final int REQUEST_CHECK_SETTINGS = 55;
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
    protected
    @BindView(R.id.map_show_distance)
    RadioGroup mMapShowDistance;
    protected
    @BindView(R.id.map_show_type)
    RadioGroup mMapShowType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_services);
        showWaitingDialog();
        presenter = new LocationServicesPresenter(this);
        ButterKnife.bind(this);
        setupMap();
        ThreadMutex.instance("LoadMap").setBusy();
        presenter.loadLocationPoints();
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

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void enableMyLocation() {
        if (map == null) return;
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(receivedLocation ->
                presenter.updateSelectedDistancePoints(receivedLocation));
        map.setOnMyLocationButtonClickListener(() -> {
            showSettingsGps();
            return false;
        });
    }

    public void showSettingsGps() {
        GoogleApiClient client = getApiClient();
        client.connect();
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build();
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(client, request);
        result.setResultCallback(locationSettingsResult -> {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(
                                LocationServicesActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });
    }

    public GoogleApiClient getApiClient() {
        return new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "GPS connected!");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "GPS connection suspended!");
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> Log.d(TAG, "GPS connection failed!"))
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location_services, menu);
        menuItemSetupDistance = menu.findItem(R.id.setup_distance);
        if (AppPreferences.instance().getSelectedLocationPointDistance() != LocationDistance.NEAREST)
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

    /**
     * Muestra un diálogo de espera mientras carga el mapa
     */
    private void showWaitingDialog() {
        waitingMapDialog = new ProgressDialogService(this);
        waitingMapDialog.setMessage(this.getResources().getString(
                R.string.waiting_map_msg));
        waitingMapDialog.setNegativeButton(R.string.btn_cancel, null);
        waitingMapDialog.setCancelable(false);
        waitingMapDialog.setCanceledOnTouchOutside(false);
        waitingMapDialog.show();
    }

    /**
     * Asigna el nivel de zoom y centrea el mapa en la ciudad de cochabamba a
     * elfec como centro
     */
    private void setupMap() {
        mHandler.postDelayed(() -> {
            if (!isFinishing()) {
                GoogleMapOptions options = new GoogleMapOptions();
                options.rotateGesturesEnabled(false)
                        .camera(new CameraPosition(new LatLng(LAT_ELFEC,
                                LNG_ELFEC), DEFAULT_ZOOM, 0, 0))
                        .tiltGesturesEnabled(false)
                        .zoomControlsEnabled(false);
                SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
                mapFragment.getMapAsync(LocationServicesActivity.this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.google_map_container, mapFragment)
                        .commit();
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
        LocationServicesActivityPermissionsDispatcher.enableMyLocationWithCheck(this);
        map.setOnMarkerClickListener(marker -> {
            lastOpenedMarker = marker;
            return false;
        });
    }

    /**
     * Crea un hilo para poner los puntos en el mapa, los pone de 10 en 10
     * utilizando el hilo principal, realizando llamadas a {@link #putPointsInMapUI}
     *
     * @param points lista de puntos
     */
    private void putPointsInMapAsync(final List<LocationPoint> points) {
        final int range = 10;
        final int delay = 70;
        if (points != null && points.size() > 0)
            new Thread(() -> {
                int size = points.size();
                int iterations = (size / range) + 1;
                int topRange;
                for (int i = 0; i < iterations; i++) {
                    topRange = range * (i + 1);
                    putPointsInMapUI(
                            points.subList(range * i, topRange < size ? topRange : size)
                            , i * delay);
                }
            }).start();
    }

    /**
     * Pone los puntos en el mapa, en el hilo de la UI, esta operación podría bloquear
     * el hilo principal si son demasiados puntos, para listas grandes utilice {@link #putPointsInMapAsync}
     *
     * @param points lista de puntos
     * @param delay  el tiempo delay en el que se pondrá en la UI los puntos
     */
    private void putPointsInMapUI(final List<LocationPoint> points, int delay) {
        mHandler.postDelayed(() -> {
            for (LocationPoint point : points) {
                addMarker(point);
            }
        }, delay);
    }


    //region Interface Methods

    @Override
    public void showLocationServicesErrors(final List<Exception> errors) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    LocationServicesActivity.this);
            builder.setTitle(R.string.errors_on_get_points_title)
                    .setMessage(MessageListFormatter.formatHTMLFromErrors(LocationServicesActivity
                            .this, errors))
                    .setPositiveButton(R.string.btn_ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            TextView txtMessage = (TextView) dialog.findViewById(android.R.id.message);
            if (txtMessage != null) {
                txtMessage.setMovementMethod(LinkMovementMethod.getInstance());
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

    @Override
    public void onLoading(@StringRes int message) {

    }

    @Override
    public void onError(Throwable e) {
        if(e instanceof OutdatedAppException)
            showErrorDialog(e);
        else showErrorToast(e);
    }

    @Override
    public void onLoaded(final List<LocationPoint> points) {
        ThreadMutex.instance("LoadMap").addOnThreadReleasedEvent(() -> {
            map.clear();
            mBitmapOffice = BitmapDescriptorFactory.fromResource(R.drawable.office_marker);
            mBitmapPayPoint = BitmapDescriptorFactory.fromResource(R.drawable.paypoint_marker);
            putPointsInMapAsync(points);
        });

    }
    //endregion

    private void showErrorDialog(Throwable e){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.errors_on_get_points_title)
                .setMessage(HtmlCompat.fromHtml(e.getMessage()))
                .setPositiveButton(R.string.btn_ok, null).create();
        dialog.show();
        TextView txtMessage = (TextView) dialog.findViewById(android.R.id.message);
        if (txtMessage != null) {
            txtMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void showErrorToast(Throwable e){
        SuperToast.create(LocationServicesActivity.this,
                e.getMessage(), SuperToast.Duration.SHORT,
                Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
    }

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
                .icon(point.getType() == LocationPointType.OFFICE ? mBitmapOffice : mBitmapPayPoint));
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
        LocationPointType selectedType = AppPreferences.instance()
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
        LocationDistance selectedDistance = AppPreferences.instance()
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
                    selectedDistance -> presenter.setDistanceRange(selectedDistance)).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap obtainedMap) {
        map = obtainedMap;
        if (waitingMapDialog != null)
            waitingMapDialog.dismiss();
        initializeMapOptions();
    }

    @Override
    public void informNoInternetConnection() {
        runOnUiThread(() -> SuperToast.create(LocationServicesActivity.this,
                R.string.no_internet_msg, SuperToast.Duration.SHORT,
                Style.getStyle(Style.BLUE, SuperToast.Animations.FADE))
                .show());
    }

}
