package com.elfec.ssc.model.enums;

/**
 * Representa los tipos de grupos de notificaciones
 * 
 * @author Diego
 *
 */
public enum NotificationType {
	/**
	 * Grupo de notificaciones de tipo de corte de luz o de informaci�nes
	 */
	OUTAGE_OR_INFO,
	/**
	 * Grupo de notificaciones de tipo de cuentas
	 */
	ACCOUNT,
	/**
	 * Grupo de otro tipo de notificaciones que no son ni cortes ni cuentas
	 */
	OTHERS;

	/**
	 * Obtiene el tipo de notificaci�n que corresponde al short provisto
	 * 
	 * @param status
	 * @return
	 */
	public static NotificationType get(short status) {
		return NotificationType.values()[status];
	}

	/**
	 * Convierte el tipo de notificaci�n a su short equivalente
	 * 
	 * @return Short equivalente al tipo de notificaci�n
	 */
	public short toShort() {
		return (short) this.ordinal();
	}
}
