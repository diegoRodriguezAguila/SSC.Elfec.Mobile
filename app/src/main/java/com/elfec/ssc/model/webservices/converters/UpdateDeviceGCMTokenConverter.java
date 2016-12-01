package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.model.webservices.ResultConverter;

public class UpdateDeviceGCMTokenConverter implements ResultConverter<Boolean> {

	@Override
	public Boolean convert(String result) {
		if (result != null) {
			return Boolean.parseBoolean(result);
		}
		return false;
	}

}
