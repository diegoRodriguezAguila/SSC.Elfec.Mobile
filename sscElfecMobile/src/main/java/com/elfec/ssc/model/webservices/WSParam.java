package com.elfec.ssc.model.webservices;

/**
 * Clase que sirve como parámetros para los servicios web
 * @author Diego
 *
 */
public class WSParam
{
	private String Key;
	private Object Value;
	
	public WSParam(String key, Object value) 
	{
		Key = key;
		Value = value;
	}
	
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public Object getValue() {
		return Value;
	}

	public void setValue(Object value) {
		Value = value;
	}
}
