package com.elfec.ssc.messaging.handlers;

import com.elfec.ssc.model.enums.NotificationKey;

import java.util.Hashtable;

/**
 * Factory para crear GCM handlers según su key
 *
 * @author Diego
 */
public class GcmHandlerFactory {
    private static Hashtable<String, Class<? extends INotificationHandler>> gcmHandlers = new Hashtable<>();

    static {
        gcmHandlers.put(NotificationKey.NEW_ACCOUNT.toString(),
                NewAccountGcmHandler.class);
        gcmHandlers.put(NotificationKey.ACCOUNT_DELETED.toString(),
                AccountDeletedGcmHandler.class);
        gcmHandlers.put(NotificationKey.CONTACTS_UPDATE.toString(),
                ContactsUpdateGcmHandler.class);
        gcmHandlers.put(NotificationKey.SCHEDULED_OUTAGE.toString(),
                OutageGcmHandler.class);
        gcmHandlers.put(NotificationKey.INCIDENTAL_OUTAGE.toString(),
                OutageGcmHandler.class);
        gcmHandlers.put(NotificationKey.NONPAYMENT_OUTAGE.toString(),
                OutageGcmHandler.class);
        gcmHandlers.put(NotificationKey.MISCELLANEOUS.toString(),
                MiscellaneousGcmHandler.class);
        gcmHandlers.put(NotificationKey.EXPIRED_DEBT.toString(),
                OutageGcmHandler.class);
    }

    /**
     * Obtiene el GCMHandler para la key dada, si no existe una clase con la key
     * retorna null
     *
     * @param key handler key identifier
     * @return handler instance
     */
    public static INotificationHandler create(String key) {
        try {
            if (key == null) return null;
            return gcmHandlers.get(key).newInstance();
        } catch (Exception e) {
            //ignore exception
        }
        return null;
    }
}
