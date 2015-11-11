package com.elfec.ssc.view.adapters;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.utils.PhoneFormatter;
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
		
		((TextView) popup.findViewById(R.id.marker_location_type)).setText(Html.fromHtml(typeAndTitle[0]));
		((TextView) popup.findViewById(R.id.marker_title)).setText(Html.fromHtml(typeAndTitle[1]));
		
		((TextView) popup.findViewById(R.id.txt_marker_address)).setText(Html.fromHtml(addressPhoneAndHours[0]));
		((TextView) popup.findViewById(R.id.txt_marker_phone)).setText(Html.fromHtml(PhoneFormatter.formatPhone(addressPhoneAndHours[1])));
		
		TextView txtHours = (TextView) popup.findViewById(R.id.txt_marker_bussines_hours);
		if(addressPhoneAndHours.length==3)
		{
			txtHours.setText(Html.fromHtml(addressPhoneAndHours[2]));
			txtHours.setVisibility(View.VISIBLE);
		}
		else
		{
			txtHours.setVisibility(View.GONE);
		}
		return (popup);
	}

}
