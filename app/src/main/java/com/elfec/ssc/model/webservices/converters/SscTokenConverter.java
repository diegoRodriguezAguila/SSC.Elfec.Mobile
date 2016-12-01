package com.elfec.ssc.model.webservices.converters;

import android.util.Log;

import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.ResultConverter;

import org.json.JSONException;
import org.json.JSONObject;

public class SscTokenConverter implements ResultConverter<SscToken> {

	@Override
	public SscToken convert(String result) {
		try {
			if(result==null)
				return null;
			JSONObject json = new JSONObject(result);
			return new SscToken(json.getString("imei"), json.getString("token"));
		} 
		catch (JSONException |NullPointerException e) {
			Log.d("Convert Exception", e.toString());
		}
		return null;
	}

}