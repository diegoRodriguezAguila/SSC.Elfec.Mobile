package com.elfec.ssc.model.webservices.converters;

import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.Contact;
import com.elfec.ssc.model.webservices.ResultConverter;

public class GetContactUpdateWSConverter implements ResultConverter<Contact> {

	@Override
	public Contact convert(String result) {
		try {
			JSONObject object = new JSONObject(result);
			return new Contact(object.getString("Phone"), object.getString("Address"), object.getString("Email"), 
					object.getString("WebPage"), object.getString("Facebook"), object.getString("FacebookId"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Contact.getDefaultContact();
	}

}
