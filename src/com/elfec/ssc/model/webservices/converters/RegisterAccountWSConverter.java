package com.elfec.ssc.model.webservices.converters;

import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.helpers.JsonToAccountConverter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class RegisterAccountWSConverter implements IWSResultConverter<Account> {

	@Override
	public Account convert(String result) {
		if (result != null) 
		{
			try 
			{
				JSONObject accObj = new JSONObject(result);
				return JsonToAccountConverter.convert(accObj);
			} 
			catch (JSONException e) {}			
		}
		return null;
	}

}
