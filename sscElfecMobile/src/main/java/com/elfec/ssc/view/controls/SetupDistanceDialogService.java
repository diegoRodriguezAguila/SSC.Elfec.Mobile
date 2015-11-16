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
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.view.controls.events.OnDistanceSetup;

import butterknife.Bind;
import butterknife.ButterKnife;

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
	private AlertDialog mDialog;
	private OnDistanceSetup mListener;
	private int mSelectedDistance;
	protected @Bind(R.id.distance_seek_bar) SeekBar mDistanceSeekBar;
	protected @Bind(R.id.txt_current_distance) TextView mTxtCurrentDistance;

	/**
	 * Construye el di�logo con lo necesario
	 * 
	 * @param context context
	 * @param listener listener
	 * @return instancia del dialogo
	 */
	public static SetupDistanceDialogService instanceService(Context context,
			OnDistanceSetup listener) {
		return new SetupDistanceDialogService(context, listener);
	}

	private SetupDistanceDialogService(final Context context,
			OnDistanceSetup listener) {
		this.mListener = listener;
		mSelectedDistance = AppPreferences.instance().getConfiguredDistance();
		View distanceSetupView = LayoutInflater.from(context).inflate(
				R.layout.distance_setup_layout, null);
		ButterKnife.bind(this, distanceSetupView);
		mDistanceSeekBar.setMax(95);
		mDistanceSeekBar.setProgress((mSelectedDistance - 500) / 100);
		mTxtCurrentDistance.setText(String.valueOf(mSelectedDistance));
		mDistanceSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					// #region unusable
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					// #endregion
					@Override
					public void onProgressChanged(SeekBar seekBar,
												  int progress, boolean fromUser) {
						mSelectedDistance = (progress * 100) + 500;
						mTxtCurrentDistance.setText(String.valueOf(mSelectedDistance));
					}
				});
		mDialog = new AlertDialog.Builder(context).setTitle(R.string.setup_distance_title)
				.setView(distanceSetupView)
				.setPositiveButton(R.string.btn_ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppPreferences.instance()
								.setConfiguredDistance(mSelectedDistance);
						if (SetupDistanceDialogService.this.mListener != null)
							SetupDistanceDialogService.this.mListener
									.onDistanceSelected(mSelectedDistance);
					}
				}).setNegativeButton(R.string.btn_cancel, null).create();
	}

	/**
	 * Muestra el di�logo construido
	 */
	public void show() {
		mDialog.show();
	}
}
