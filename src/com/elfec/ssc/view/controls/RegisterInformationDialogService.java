package com.elfec.ssc.view.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;

@SuppressLint("InflateParams")
public class RegisterInformationDialogService {

	private AlertDialogPro mDialog;

	public RegisterInformationDialogService(Context context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.register_account_info_layout, null);
		mDialog = new AlertDialogPro.Builder(context)
				.setTitle(R.string.title_register_account_information)
				.setIcon(R.drawable.information_d).setView(rootView)
				.setPositiveButton(R.string.btn_understanded, null).create();
	}

	/**
	 * Muestra el diálogo construido
	 */
	public void show() {
		mDialog.show();
	}
}
