package com.elfec.ssc.model.enums;

/**
 * Representa los distintos estados de un cliente a nivel local
 * @author Diego
 *
 */
public enum ClientStatus {

	/**
	 * Estado cuando un cliente fué registrado en la aplicación pero otro es el que esta activo
	 */
	REGISTERED("Registrado"), 
	/**
	 * Estado cuando un cliente es el activo y que se utiliza para la aplicación
	 */
	ACTIVE("Activo");
	/**
	 * Obtiene el estado del cliente equivalente al short provisto
	 * @param status
	 * @return
	 */
	public static ClientStatus get(short status)
	{
		return ClientStatus.values()[status];
	}
	
	private String string;
	private ClientStatus(String string)
	{
		this.string = string;
	}
	
	@Override
	public String toString() {
	       return string;
	   }
	
	/**
	 * Convierte el estado del cliente actual a su short equivalente
	 * @return Short equivalente al estado
	 */
	public short toShort()
	{
		return (short)this.ordinal();
	}
}
