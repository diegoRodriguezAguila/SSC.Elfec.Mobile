package com.elfec.ssc.view.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.security.PreferencesManager;
import com.elfec.ssc.view.controls.events.OnDistanceSetup;

/**
 * Esta clase provee de un servicio para mostrar un di�logo para la
 * configuraci�n de distancia en los servicios de ubicaci�n, cuando se
 * selecciona la opci�n aceptar guarda la distancia elegida en sharedpreferences
 * 
 * @author Diego
 *
 */
@SuppressLint("InflateParams")
public class SetupDistanceDialogService {
	private AlertDialog.Builder dialogBuilder;
	private OnDistanceSetup listener;
	private int selectedDistance;
	private SeekBar distanceSeekBar;
	private TextView txtCurrentDistance;
	private PreferencesManager preferencesManager;

	/**
	 * Construye el di�logo con lo necesario
	 * 
	 * @param context
	 * @param listener
	 * @return
	 */
	public static SetupDistanceDialogService instanceService(Context context,
			OnDistanceSetup listener) {
		return new SetupDistanceDialogService(context, listener);
	}

	private SetupDistanceDialogService(final Context context,
			OnDistanceSetup listener) {
		this.listener = listener;
		preferencesManager = new PreferencesManager(context);
		selectedDistance = preferencesManager.getConfiguredDistance();

		View distanceSetupView = LayoutInflater.from(context).inflate(
				R.layout.distance_setup_layout, null);
		distanceSeekBar = (SeekBar) distanceSetupView
				.findViewById(R.id.distance_seek_bar);
		txtCurrentDistance = (TextView) (distanceSetupView
				.findViewById(R.id.txt_current_distance));
		distanceSeekBar.setMax(95);
		distanceSeekBar.setProgress((selectedDistance - 500) / 100);
		txtCurrentDistance.setText(selectedDistance + "");
		distanceSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					// #region unusable
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {}

					// #endregion
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						selectedDistance = (progress * 100) + 500;
						txtCurrentDistance.setText(selectedDistance + "");
					}
				});
		dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle(R.string.setup_distance_title)
				.setView(distanceSetupView)
				.setPositiveButton(R.string.btn_ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						preferencesManager
								.setConfiguredDistance(selectedDistance);
						if (SetupDistanceDialogService.this.listener != null)
							SetupDistanceDialogService.this.listener
									.onDistanceSelected(selectedDistance);
					}
				}).setNegativeButton(R.string.btn_cancel, null);
	}

	/**
	 * Muestra el di�logo construido
	 */
	public void show() {
		dialogBuilder.show();
	}
}
