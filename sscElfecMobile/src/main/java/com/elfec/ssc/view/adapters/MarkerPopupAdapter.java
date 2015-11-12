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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MarkerPopupAdapter implements InfoWindowAdapter {

	private View mPopupView;
    protected @Bind(R.id.marker_location_type) TextView mTxtLocationType;
    protected @Bind(R.id.marker_title) TextView mTxtTitle;
    protected @Bind(R.id.txt_marker_address) TextView mTxtAddress;
    protected @Bind(R.id.txt_marker_phone) TextView mTxtPhone;
    protected @Bind(R.id.txt_marker_bussines_hours) TextView mTxtBussiness;

    private LayoutInflater inflater;

    public MarkerPopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }
	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@SuppressLint("InflateParams") @Override
	public View getInfoContents(Marker marker) {
		if (mPopupView == null) {
			mPopupView = inflater.inflate(R.layout.marker_popup, null);
            ButterKnife.bind(this, mPopupView);
		}
		String markerTitle = marker.getTitle();
		String markerSnippet = marker.getSnippet();
		String[] typeAndTitle = markerTitle.split("\n");
		final String[] addressPhoneAndHours = markerSnippet.split("\n");
        mTxtLocationType.setText(Html.fromHtml(typeAndTitle[0]));
        mTxtTitle.setText(Html.fromHtml(typeAndTitle[1]));
        mTxtAddress.setText(Html.fromHtml(addressPhoneAndHours[0]));
        mTxtPhone.setText(Html.fromHtml(PhoneFormatter.formatPhone(addressPhoneAndHours[1])));
		if(addressPhoneAndHours.length==3) {
            mTxtBussiness.setText(Html.fromHtml(addressPhoneAndHours[2]));
            mTxtBussiness.setVisibility(View.VISIBLE);
		}
		else mTxtBussiness.setVisibility(View.GONE);
		return mPopupView;
	}

}
