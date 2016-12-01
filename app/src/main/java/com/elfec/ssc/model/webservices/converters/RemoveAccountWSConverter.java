package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.model.webservices.ResultConverter;

public class RemoveAccountWSConverter implements ResultConverter<Boolean> {

	@Override
	public Boolean convert(String result) {
		return result != null && Boolean.parseBoolean(result);
	}

}
