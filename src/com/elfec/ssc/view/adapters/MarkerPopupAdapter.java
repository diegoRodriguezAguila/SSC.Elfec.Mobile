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
		String markerSnippet = marker.getSnippet();
		String[] typeAndTitle = markerTitle.split("\n");
		final String[] addressPhoneAndHours = markerSnippet.split("\n");
		
		((TextView) popup.findViewById(R.id.marker_location_type)).setText(typeAndTitle[0]);
		((TextView) popup.findViewById(R.id.marker_title)).setText(typeAndTitle[1]);
		
		((TextView) popup.findViewById(R.id.txt_marker_address)).setText(addressPhoneAndHours[0]);
		TextView txtPhone = (TextView) popup.findViewById(R.id.txt_marker_phone);
		txtPhone.setText(addressPhoneAndHours[1]);
		
		TextView txtHours = (TextView) popup.findViewById(R.id.txt_marker_bussines_hours);
		TextView lblHours = (TextView) popup.findViewById(R.id.lbl_marker_bussines_hours);
		if(addressPhoneAndHours.length==3)
		{
			txtHours.setText(addressPhoneAndHours[2]);
			txtHours.setVisibility(View.VISIBLE);
			lblHours.setVisibility(View.VISIBLE);
		}
		else
		{
			txtHours.setVisibility(View.GONE);
			lblHours.setVisibility(View.GONE);
		}
		return (popup);
	}

}
