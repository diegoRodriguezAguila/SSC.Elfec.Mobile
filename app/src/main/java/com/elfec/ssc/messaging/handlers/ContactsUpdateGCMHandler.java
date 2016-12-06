package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.elfec.ssc.business_logic.ContactManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.presenter.ContactPresenter;
import com.elfec.ssc.view.ContactsActivity;

import rx.schedulers.Schedulers;

/**
 * Maneja las notificaciones de actualización de contactos
 *
 * @author drodriguez
 */
public class ContactsUpdateGCMHandler implements INotificationHandler {

    private static final int NOTIF_ID = 4;

    @Override
    public void handleNotification(Bundle message,
                                   NotificationManager notifManager, Builder builder) {
        ContactManager.syncContact()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(contact -> {
                    ContactPresenter presenter = ViewPresenterManager.getPresenter(ContactPresenter.class);
                    notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
                    if (presenter != null)
                        presenter.loadContact();
                }, e -> {
                });
    }

    @Override
    public Class<? extends Activity> getActivityClass() {
        return ContactsActivity.class;
    }

}
