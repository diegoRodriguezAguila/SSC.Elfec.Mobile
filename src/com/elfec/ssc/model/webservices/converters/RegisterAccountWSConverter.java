package com.elfec.ssc.model.webservices.converters;

import com.activeandroid.util.Log;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RegisterAccountWSConverter implements IWSResultConverter<Boolean> {

	@Override
	public Boolean convert(String result) {
		Log.d("WEB SERVICE","RESULT: "+result);
		return null;
	}

}
