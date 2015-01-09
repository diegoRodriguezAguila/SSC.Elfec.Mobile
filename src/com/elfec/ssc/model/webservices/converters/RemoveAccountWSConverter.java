package com.elfec.ssc.model.webservices.converters;

import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RemoveAccountWSConverter implements IWSResultConverter<Boolean> {

	@Override
	public Boolean convert(String result) {
		
		return Boolean.parseBoolean(result);
		
	}

}
