package com.elfec.ssc.local_storage;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;

/**
 * Created by drodriguez on 01/12/2016.
 * NotificationStorage
 */

public class NotificationStorage {

    private static final String NOTIFICATIONS_BOOK = "notifications.book";
    private static final String NOTIFICATIONS_KEY = "notifications_%1$d";

    /**
     * Saves an notification to the database in their types category
     *
     * @param notification to save
     * @return Observable of notification
     */
    public Observable<Notification> saveNotification(Notification notification) {
        return getNotifications(notification.getType())
                .flatMap(notifications -> {
                    if (notifications == null)
                        notifications = new ArrayList<>();
                    if (!notifications.contains(notification))
                        notifications.add(0, notification);
                    return saveNotifications(notification.getType(), notifications);
                }).map(m -> notification);
    }

    /**
     * Saves notifications in a certain type category, it doesn't validate
     * the notifications' types so it must be called carefully
     *
     * @param type          notification type
     * @param notifications to save
     * @return observable of the saved list
     */
    private Observable<List<Notification>> saveNotifications(NotificationType type,
                                                             List<Notification> notifications) {
        return RxPaper.book(NOTIFICATIONS_BOOK)
                .write(key(type), notifications);
    }

    /**
     * Clears the notifications of certain type
     *
     * @param type notification type
     * @return observable of the removed notifications
     */
    public Observable<List<Notification>> removeNotifications(NotificationType type) {
        return RxPaper.book(NOTIFICATIONS_BOOK).write(key(type), new ArrayList<>());
    }

    /**
     * Retrieves notifications from database
     *
     * @param type notification type
     * @return Observable of a Notifications
     */
    public Observable<List<Notification>> getNotifications(NotificationType type) {
        return RxPaper.book(NOTIFICATIONS_BOOK).read(key(type));
    }

    /**
     * Retrieves notifications from database, limited in the size
     *
     * @param type  notification type
     * @param limit limit of the array
     * @return Observable of a Notifications
     */
    public Observable<List<Notification>> getNotifications(NotificationType type, int limit) {
        return getNotifications(type)
                .map(notifications -> {
                    if (notifications == null)
                        return null;
                    if (notifications.size() < limit || limit < 0)
                        return notifications;
                    return notifications.subList(0, limit);
                });
    }

    private String key(NotificationType type) {
        return String.format(Locale.getDefault(), NOTIFICATIONS_KEY, type.toShort());
    }
}
