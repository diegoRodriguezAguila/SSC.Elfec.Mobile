package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.elfec.ssc.business_logic.AccountManager;
import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.NotificationManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.utils.ObjectsCompat;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.presenter.AccountsPresenter;
import com.elfec.ssc.presenter.NotificationsPresenter;
import com.elfec.ssc.view.AccountsActivity;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Maneja las notificaciones de eliminación de cuentas
 *
 * @author drodriguez
 */
public class AccountDeletedGcmHandler implements INotificationHandler {

    private static final int NOTIF_ID = 2;

    @Override
    public Class<? extends Activity> getActivityClass() {
        return AccountsActivity.class;
    }

    @Override
    public void handleNotification(Bundle message, android.app.NotificationManager notifManager, NotificationCompat.Builder builder) {
        ClientManager.activeClient()
                .flatMap(client -> {
                    String gmail = message.getString("gmail");
                    if (client == null || !ObjectsCompat.equals(gmail, client.getGmail()) ||
                            message.getString("nus") == null)
                        return Observable.just(null);
                    return AccountManager.removeAccount(gmail,
                            new Account(message.getString("nus")));
                })
                .flatMap(acc -> {
                    if (acc == null)
                        return Observable.just(null);
                    return NotificationManager.saveNotification(new Notification(message));
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(notification -> {
                    //Si la vista de ver cuentas esta activa
                    AccountsPresenter presenter = ViewPresenterManager
                            .getPresenter(AccountsPresenter.class);
                    if (presenter != null)
                        presenter.loadAccounts();
                    //Si la vista de ver notificaciones está activa
                    NotificationsPresenter notifPresenter = ViewPresenterManager
                            .getPresenter(NotificationsPresenter.class);
                    if (notifPresenter != null)
                        notifPresenter.addNewAccountNotificationUpdate(notification);
                    notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
                }, e -> {
                });
    }

}
