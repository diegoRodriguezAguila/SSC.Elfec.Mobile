package com.elfec.ssc.model.enums;

/**
 * Enum que sirve para diferenciar los distintos tipos de puntos de ubicación
 * según la proximidad al cliente
 * @author Diego
 *
 */
public enum LocationDistance {
	/**
	 * Tipo de punto que especifica los puntos cercanos
	 */
	NEAR("Cercanos"), 
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
