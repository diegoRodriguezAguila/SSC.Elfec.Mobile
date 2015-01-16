package com.elfec.ssc.view.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MarkerPopupAdapter implements InfoWindowAdapter {

	private View popup=null;
    private LayoutInflater inflater=null;

    public MarkerPopupAdapter(LayoutInflater inflater) {
       this.inflater=inflater;
    }
	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@SuppressLint("InflateParams") @Override
	public View getInfoContents(Marker marker) {
		if (popup == null) {
			popup = inflater.inflate(R.layout.marker_popup, null);
		}
		String markerTitle = marker.getTitle();
		String[] typeAndTitle = markerTitle.split("\n");
		((TextView) popup.findViewById(R.id.marker_location_type)).setText(typeAndTitle[0]);
		((TextView) popup.findViewById(R.id.marker_title)).setText(typeAndTitle[1]);
		
		//txtTitle = (TextView) popup.findViewById(R.id.marker_snippet);
		//txtTitle.setText(marker.getSnippet());
		return (popup);
	}

}
