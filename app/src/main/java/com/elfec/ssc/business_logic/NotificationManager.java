package com.elfec.ssc.business_logic;

import com.elfec.ssc.local_storage.NotificationStorage;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationType;

import java.util.List;

import rx.Observable;

/**
 * Maneja las distintas operaciones de Notificaciones
 *
 * @author Diego
 */
public class NotificationManager {

    /**
     * Saves a notification
     *
     * @param notification to save
     * @return observable of notification
     */
    public static Observable<Notification> saveNotification(Notification notification) {
        return new NotificationStorage().saveNotification(notification);
    }

    public static Observable<List<Notification>> removeNotifications(NotificationType type) {
        return new NotificationStorage().removeNotifications(type);
    }

}
