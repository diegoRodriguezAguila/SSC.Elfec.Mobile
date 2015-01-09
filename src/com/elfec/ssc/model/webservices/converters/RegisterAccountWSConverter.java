package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RegisterAccountWSConverter implements IWSResultConverter<Boolean> {

	@Override
	public Boolean convert(String result) {
		if (result != null) {
			return Boolean.parseBoolean(result);
		}
		return false;
	}

}
