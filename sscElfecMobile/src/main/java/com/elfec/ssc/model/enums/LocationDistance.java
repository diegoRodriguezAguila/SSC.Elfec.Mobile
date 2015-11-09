package com.elfec.ssc.model.enums;

/**
 * Enum que sirve para diferenciar los distintos tipos de puntos de ubicaci�n
 * seg�n la proximidad al cliente
 * @author Diego
 *
 */
public enum LocationDistance {
	/**
	 * Tipo de punto que especifica los puntos cercanos
	 */
	NEAREST("Cercanos"), 
	/**
	 * Tipo de punto indefinido
	 */
	ALL("Todos");
	private String string;
	private LocationDistance(String string)
	{
		this.string = string;
	}
	
	public static LocationDistance get(short type)
	{
		return LocationDistance.values()[type];
	}
	
	public short toShort()
	{
		return (short)this.ordinal();
	}
	
	@Override
	public String toString() {
	       return string;
	   }
	
}
