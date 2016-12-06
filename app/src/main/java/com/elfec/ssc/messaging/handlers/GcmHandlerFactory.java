package com.elfec.ssc.messaging.handlers;

import com.elfec.ssc.model.enums.NotificationKey;

import java.util.Hashtable;

/**
 * Factory para crear GCM handlers según su key
 * 
 * @author Diego
 *
 */
public class GcmHandlerFactory {
	private static Hashtable<String, Class<? extends INotificationHandler>> gcmHandlers = new Hashtable<>();
	static {
		gcmHandlers.put(NotificationKey.NEW_ACCOUNT.toString(),
				NewAccountGCMHandler.class);
		gcmHandlers.put(NotificationKey.ACCOUNT_DELETED.toString(),
				AccountDeletedGCMHandler.class);
		gcmHandlers.put(NotificationKey.POINTS_UPDATE.toString(),
				PointsUpdateGCMHandler.class);
		gcmHandlers.put(NotificationKey.CONTACTS_UPDATE.toString(),
				ContactsUpdateGCMHandler.class);
		gcmHandlers.put(NotificationKey.SCHEDULED_OUTAGE.toString(),
				OutageGCMHandler.class);
		gcmHandlers.put(NotificationKey.INCIDENTAL_OUTAGE.toString(),
				OutageGCMHandler.class);
		gcmHandlers.put(NotificationKey.NONPAYMENT_OUTAGE.toString(),
				OutageGCMHandler.class);
		gcmHandlers.put(NotificationKey.MISCELLANEOUS.toString(),
				MiscellaneousGCMHandler.class);
		gcmHandlers.put(NotificationKey.EXPIRED_DEBT.toString(),
				OutageGCMHandler.class);
	}

	/**
	 * Obtiene el GCMHandler para la key dada, si no existe una clase con la key
	 * retorna null
	 * 
	 * @param key
	 * @return
	 */
	public static INotificationHandler create(String key) {
		try {
			return gcmHandlers.get(key).newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (NullPointerException e) {
		}
		return null;
	}
}
