package com.elfec.ssc.model.webservices.converters;

import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RequestWSTokenConverter implements IWSResultConverter<WSToken>{

	@Override
	public WSToken convert(String result) {
		try {
			JSONObject json = new JSONObject(result);
			return new WSToken(json.getString("imei"), json.getString("token"));
		} 
		catch (JSONException e) {}
		catch (NullPointerException e) {}
		return null;
	}

}
