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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Esta clase provee de un servicio para mostrar un diálogo para la
 * configuración de distancia en los servicios de ubicación, cuando se
 * selecciona la opción aceptar guarda la distancia elegida en sharedpreferences
 *
 * @author Diego
 *
 */
@SuppressLint("InflateParams")
public class SetupDistanceDialogService {
	private AlertDialog mDialog;
	private OnDistanceSetup mListener;
	private int mSelectedDistance;
	protected @BindView(R.id.distance_seek_bar) SeekBar mDistanceSeekBar;
	protected @BindView(R.id.txt_current_distance) TextView mTxtCurrentDistance;

	/**
	 * Construye el diálogo con lo necesario
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
					//region unusable
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					//endregion
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
	 * Muestra el diálogo construido
	 */
	public void show() {
		mDialog.show();
	}
}
