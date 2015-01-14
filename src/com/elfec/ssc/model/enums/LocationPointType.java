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
	OFFICE, 
	/**
	 * Tipo de punto de ubicación de un punto de pago
	 */
	PAYPOINT;
	/**
	 * Obtiene el tipo del punto de ubicación equivalente al short provisto
	 * @param type
	 * @return
	 */
	public static LocationPointType get(short type)
	{
		return LocationPointType.values()[type];
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
