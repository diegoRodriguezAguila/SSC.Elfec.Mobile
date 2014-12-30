package com.elfec.ssc.model.webservices;

import java.util.List;

import com.activeandroid.util.Log;

public class RegisterAccountWSConverter implements IWSResultConverter<List<Integer>> {

	@Override
	public List<Integer> convert(String result) {
		Log.d("WEB SERVICE","RESULT: "+result);
		return null;
	}

}
