package com.elfec.ssc.view.controls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.view.controls.events.OnDistanceSetup;

@SuppressLint("InflateParams")
public class SetupDistanceDialogService {
	private AlertDialogPro.Builder dialogBuilder;
	private OnDistanceSetup listener;
	private int selectedDistance;
	private SeekBar distanceSeekBar;
	private TextView txtCurrentDistance;
	public static SetupDistanceDialogService instanceService(Activity activity,OnDistanceSetup listener)
	{
		return new SetupDistanceDialogService(activity,listener);
	}
	private SetupDistanceDialogService(final Activity activity,OnDistanceSetup listener)
	{
		this.listener=listener;
		selectedDistance=1000;
		
		View distanceSetupView=activity.getLayoutInflater().inflate(R.layout.distance_setup_layout, null);
		distanceSeekBar= (SeekBar)distanceSetupView.findViewById(R.id.distance_seek_bar);
		txtCurrentDistance=(TextView)(distanceSetupView.findViewById(R.id.txt_current_distance));
		distanceSeekBar.setMax(9500);
		distanceSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			//#region unusable
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			//#endregion
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				selectedDistance=progress+500;
				txtCurrentDistance.setText(selectedDistance+"");
			}
		});
		dialogBuilder = new AlertDialogPro.Builder(activity);
		dialogBuilder.setTitle(R.string.account_picker_title).setView(distanceSetupView)
        .setPositiveButton(R.string.btn_ok, new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				SetupDistanceDialogService.this.listener.onDistanceSelected(selectedDistance);
			}		
		})
		.setNegativeButton(R.string.btn_cancel, null);
	}
	public void show()
	{
		dialogBuilder.show();
	}
}
