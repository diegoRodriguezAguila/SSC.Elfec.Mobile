package com.elfec.ssc.model.webservices.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.PayPoint;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class GetAllPayPointsConverter implements
		IWSResultConverter<List<PayPoint>> {

	@Override
	public List<PayPoint> convert(String result) {
		List<PayPoint> payPoints=new ArrayList<PayPoint>();
		if (result != null) {
			try {
				JSONArray array = new JSONArray(result);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					payPoints.add(new PayPoint(object.getString("address"), object.getString("phone"), object.getString("start_attention"), 
							object.getString("end_attention"), object.getDouble("latitude"), object.getDouble("longitude")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}	
		return payPoints;
	}

}
