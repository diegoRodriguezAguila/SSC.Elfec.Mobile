package com.elfec.ssc.helpers.ui;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Helper para acciones del teclado
 * 
 * @author drodriguez
 *
 */
public class KeyboardHelper {

	/**
	 * Esconde el teclado
	 */
	public static void hideKeyboard(View rootView) {
		if (rootView != null) {
			// Check if no view has focus:
			View view = rootView.findFocus();
			if (view != null) {
				InputMethodManager inputManager = (InputMethodManager) rootView
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * Muestra el teclado
	 * 
	 * @param view
	 */
	public static void showKeyboard(final View view) {
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager) view
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(view, 0);
			}
		}, 200);
	}
}
