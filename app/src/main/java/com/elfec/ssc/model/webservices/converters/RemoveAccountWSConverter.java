package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RemoveAccountWSConverter implements IWSResultConverter<Boolean> {

	@Override
	public Boolean convert(String result) {
		return result != null && Boolean.parseBoolean(result);
	}

}
