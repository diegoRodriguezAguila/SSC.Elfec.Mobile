package com.elfec.ssc.view.controls.events;

import com.google.android.gms.maps.GoogleMap;

/**
 * Provee de un evento para cuando se carga un supportmapfragment
 * @author Diego
 *
 */
public interface OnMapReadyCallback {
	public void onMapReady(GoogleMap obtainedMap);
}
