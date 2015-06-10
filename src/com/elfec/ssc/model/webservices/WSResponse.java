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
	/**
	 * Obtiene la lista de errores del resultado de un servicio web
	 * @return Lista de errores del WS
	 */
	public List<Exception> getErrors()
	{
		return listOfErrors;
	}
	/**
	 * Agrega un error a la lista de errores del resultado de un servicio web
	 * @param e
	 */
	public void addError(Exception e)
	{
		listOfErrors.add(e);
	}
	/**
	 * Convierte un json y extrae la lista de errores
	 * retornando el mismo json sin la lista de errores
	 * @param response
	 * @return
	 */
	public String convertErrors(String response)
	{
		try {
			JSONObject mainObject = (JSONObject) new JSONObject(response);
			JSONArray array = mainObject.getJSONArray("Errors");
			for(int i=0;i<array.length();i++)
			{
				JSONObject object = (JSONObject)array.get(i);
				listOfErrors.add(new Exception(object.getString("message")));
			}
			return mainObject.get("Response").toString();
		} catch (JSONException e) {
		}
		return null;
	}
	/**
	 * Asigna el resultado a un servicio web
	 * @param result
	 * @return this instance
	 */
	public WSResponse<TResult> setResult(TResult result)
	{
		this.result = result;
		return this;
	}
	
	/**
	 * Obtiene el resultado de un servicio web
	 * @return Resultado del ws
	 */
	public TResult getResult()
	{
		return this.result;
	}
	
	/**
	 * Si es que el resultado del servicio web tuvo errores
	 * @return
	 */
	public boolean hasErrors()
	{
		return listOfErrors.size() > 0;
	}
}
