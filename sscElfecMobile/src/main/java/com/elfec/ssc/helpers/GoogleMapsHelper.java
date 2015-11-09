package com.elfec.ssc.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Helper encargado de funciones de google maps
 * 
 * @author drodriguez
 *
 */
public class GoogleMapsHelper {
	/**
	 * Abre google maps con la posición y el marker indicados
	 * 
	 * @param context
	 * @param latitude
	 * @param longitude
	 * @param label
	 */
	public static void launchGoogleMaps(Context context, double latitude,
			double longitude, String label) {
		String format = "geo:0,0?q=" + Double.toString(latitude) + ","
				+ Double.toString(longitude) + "(" + label + ")";
		Uri uri = Uri.parse(format);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}
