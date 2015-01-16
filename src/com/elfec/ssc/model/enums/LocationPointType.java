package com.elfec.ssc.model.enums;

/**
 * Representa los distintos tipos de puntos de ubicación, tanto localmente como en el servidor
 * @author Diego
 *
 */
public enum LocationPointType {
	/**
	 * Tipo de punto de ubicación de una oficina
	 */
	OFFICE("Oficina"), 
	/**
	 * Tipo de punto de ubicación de un punto de pago
	 */
	PAYPOINT("Punto de pago"),
	/**
	 * Tipo de punto de ubicación indefinido
	 */
	ALL("Todos");
	/**
	 * Obtiene el tipo del punto de ubicación equivalente al short provisto
	 * @param type
	 * @return
	 */
	public static LocationPointType get(short type)
	{
		return LocationPointType.values()[type];
	}
	
	private String string;
	private LocationPointType(String string)
	{
		this.string = string;
	}
	
	@Override
	public String toString() {
	       return string;
	   }
	
	/**
	 * Convierte el tipo de punto de ubicación actual a su short equivalente
	 * @return Short equivalente al tipo
	 */
	public short toShort()
	{
		return (short)this.ordinal();
	}
}
