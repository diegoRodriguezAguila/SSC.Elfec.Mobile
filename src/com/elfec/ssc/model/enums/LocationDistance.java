package com.elfec.ssc.model.enums;

public enum LocationDistance {
	/**
	 * Tipo de punto que especifica los puntos cercanos
	 */
	Near("Cercanos"), 
	/**
	 * Tipo de punto indefinido
	 */
	All("Todos");
	private String string;
	private LocationDistance(String string)
	{
		this.string = string;
	}
	@Override
	public String toString() {
	       return string;
	   }
	
}
