package com.elfec.ssc.model.enums;

/**
 * Representa las distintas keys de notificaciones
 * 
 * @author Diego
 *
 */
public enum NotificationKey {
	/**
	 * Notificación de que se agregó una nueva cuenta para ese cliente en otro
	 * dispositivo
	 */
	NEW_ACCOUNT("NewAccount"),
	/**
	 * Notificación de que se eliminó una cuenta del cliente en otro dispositivo
	 */
	ACCOUNT_DELETED("AccountDeleted"),
	/**
	 * Notificación de que se añadieron puntos de ubicación
	 */
	POINTS_UPDATE("PointsUpdate"),
	/**
	 * Notificación de que se actualizaron los contactos de la empresa
	 */
	CONTACTS_UPDATE("ContactsUpdate"),
	/**
	 * Notificación de corte programado
	 */
	SCHEDULED_OUTAGE("ScheduledOutage"),
	/**
	 * Notificación de corte fortuito por incidente
	 */
	INCIDENTAL_OUTAGE("IncidentalOutage"),
	/**
	 * Notificacion de corte por no pago
	 */
	NONPAYMENT_OUTAGE("NonpaymentOutage"),
	/**
	 * Key de Notificaci´´on indefinido
	 */
	MISCELLANEOUS("Miscellaneous"),
	/**
	 * Notificación de vencimiento de factura
	 */
	EXPIRED_DEBT("ExpiredDebt");

	private String string;

	NotificationKey(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}

	/**
	 * Obtiene la key de notificación que corresponde al short provisto
	 * 
	 * @param status status
	 * @return enum that matches the short status
	 */
	public static NotificationKey get(short status) {
		return NotificationKey.values()[status];
	}

	/**
	 * Obtiene el NotificationKey correspondiente a la key en cadena provista
	 * 
	 * @param keyAsString status string key
	 * @return enum that matches the string status
	 */
	public static NotificationKey get(String keyAsString) {
		NotificationKey[] notifKeys = NotificationKey.values();
		for (NotificationKey notifKey : notifKeys) {
			if (notifKey.toString().equals(keyAsString))
				return notifKey;
		}
		return NotificationKey.MISCELLANEOUS;
	}

	/**
	 * Convierte la key de notificación a su short equivalente
	 * 
	 * @return Short equivalente a la key de notificación
	 */
	public short toShort() {
		return (short) this.ordinal();
	}
}
