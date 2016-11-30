package com.elfec.ssc.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.elfec.ssc.R;

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
	 * @param context contexto
	 * @param latitude latitud
	 * @param longitude longitud
	 * @param label texto
	 */
	public static void launchGoogleMaps(Context context, double latitude,
			double longitude, String label) {
        if(latitude==0 && longitude==0)
            showInvalidLocationDialog(context);
        else {
            String format = "geo:0,0?q=" + Double.toString(latitude) + ","
                    + Double.toString(longitude) + "(" + label + ")";
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
	}

	/**
	 * Muestra un dialogo de que la ubicación no es válida o no está definida correctamente
	 * @param context contexto
	 */
	private static void showInvalidLocationDialog(Context context){
        new AlertDialog.Builder(context).setTitle(R.string.title_no_location_avaible)
                .setMessage(R.string.msg_no_location_avaible)
                .setPositiveButton(R.string.btn_ok, null ).show();
	}
}
