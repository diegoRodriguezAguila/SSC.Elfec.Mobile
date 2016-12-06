package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.NotificationManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.utils.ObjectsCompat;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.presenter.NotificationsPresenter;
import com.elfec.ssc.view.NotificationsActivity;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Maneja todos los tipos de notificaciones relacionadas a cortes
 *
 * @author drodriguez
 */
public class OutageGcmHandler implements INotificationHandler {

    private static final int NOTIF_ID = 3;

    @Override
    public void handleNotification(Bundle message,
                                   android.app.NotificationManager notifManager, Builder builder) {
        ClientManager.activeClient()
                .flatMap(client -> {
                    String gmail = message.getString("gmail");
                    if (client == null || !ObjectsCompat.equals(gmail, client.getGmail()))
                        return Observable.just(null);
                    return NotificationManager.saveNotification(new Notification(message));
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(notification -> {
                    if (notification == null) return;
                    //Si la vista de ver notificaciones está activa
                    NotificationsPresenter notifPresenter = ViewPresenterManager
                            .getPresenter(NotificationsPresenter.class);
                    if (notifPresenter != null)
                        notifPresenter.addNewOutageNotificationUpdate(notification);
                    notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
                }, e -> {
                });
    }

    @Override
    public Class<? extends Activity> getActivityClass() {
        return NotificationsActivity.class;
    }

}
