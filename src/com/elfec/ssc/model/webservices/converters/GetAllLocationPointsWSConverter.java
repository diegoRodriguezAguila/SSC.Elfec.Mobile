package com.elfec.ssc.model.webservices.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class GetAllLocationPointsWSConverter implements
		IWSResultConverter<List<LocationPoint>> {

	@Override
	public List<LocationPoint> convert(String result) {
		List<LocationPoint> payPoints=new ArrayList<LocationPoint>();
		if (result != null) {
			try {
				JSONArray array = new JSONArray(result);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					payPoints.add(new LocationPoint(object.getString("institution_name"),object.getString("address"), 
							object.getString("phone"), object.isNull("start_attention")?null:object.getString("start_attention"), 
									object.isNull("end_attention")?null:object.getString("end_attention"), object.getDouble("latitude"), 
							object.getDouble("longitude"),Short.parseShort(object.getString("type"))));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}	
		return payPoints;
	}

}
