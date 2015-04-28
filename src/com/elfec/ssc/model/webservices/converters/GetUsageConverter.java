package com.elfec.ssc.model.webservices.converters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.webservices.IWSResultConverter;

public class GetUsageConverter implements IWSResultConverter<List<Usage>> {

	@Override
	public List<Usage> convert(String result) {
		if(result!=null)
		{
			List<Usage> usage=new ArrayList<Usage>();
			try
			{
				JSONArray array = new JSONArray(result);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					usage.add(new Usage(object.getInt("EnergyUsage"), object.getString("Term")));
				}
			}
			catch(JSONException ex)
			{
				ex.printStackTrace();
			}
			return usage;
		}
		return null;
	}
}