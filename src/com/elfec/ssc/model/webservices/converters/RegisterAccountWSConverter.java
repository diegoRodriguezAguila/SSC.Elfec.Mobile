package com.elfec.ssc.model.webservices.converters;

import java.util.List;

import com.activeandroid.util.Log;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RegisterAccountWSConverter implements IWSResultConverter<List<Integer>> {

	@Override
	public List<Integer> convert(String result) {
		Log.d("WEB SERVICE","RESULT: "+result);
		return null;
	}

}
