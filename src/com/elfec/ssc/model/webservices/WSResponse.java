package com.elfec.ssc.model.webservices;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Es una clase que contiene el resultado de un servicio web adicionalmente de una 
 * lista de errores en caso de haber ocurrido alguno
 * @author Diego
 *
 * @param <TResult>
 */
public class WSResponse<TResult> {

	private TResult result;
	private List<Exception> listOfErrors;
	
	public WSResponse() {
		listOfErrors = new ArrayList<Exception>();
	}
	
	public void addError(Exception e)
	{
		listOfErrors.add(e);
	}
	
	public String convertErrors(String response)
	{
		try {
			JSONObject mainObject = (JSONObject) new JSONObject(response);
			JSONArray array = mainObject.getJSONArray("errors");
			for(int i=0;i<array.length();i++)
			{
				JSONObject object = (JSONObject)array.get(i);
				listOfErrors.add(new Exception(object.getString("message")));
			}
			return mainObject.get("response").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public WSResponse<TResult> setResult(TResult result)
	{
		this.result = result;
		return this;
	}
	
	public TResult getResult()
	{
		return this.result;
	}
	
	public boolean hasErrors()
	{
		return listOfErrors.size() > 0;
	}
}
