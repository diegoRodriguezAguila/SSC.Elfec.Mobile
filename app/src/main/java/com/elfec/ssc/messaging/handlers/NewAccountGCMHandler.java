package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.view.AccountsActivity;

/**
 * Maneja las notificaciones GCM de nuevas cuentas
 *
 * @author Diego
 */
public class NewAccountGCMHandler implements INotificationHandler {

    private static final int NOTIF_ID = 1;

    @Override
    public Class<? extends Activity> getActivityClass() {
        return AccountsActivity.class;
    }

    @Override
    public void handleNotification(Bundle messageInfo, android.app.NotificationManager notifManager, NotificationCompat.Builder builder) {
        Client ownerClient = null; //TODO current client Client.getClientByGmail(messageInfo
        // .getString("gmail"));
        if (ownerClient != null && ownerClient.getStatus() == ClientStatus.ACTIVE) {
            try {
               // AccountManager.registerAccount(JsonToAccountConverter.convert(messageInfo
                      //  .getString("account")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*Notification notif = NotificationManager.SaveNotification(messageInfo.getString
                    ("title"), messageInfo.getString("message"),
                    NotificationType.get(Short.parseShort(messageInfo.getString("type"))), NotificationKey.get(messageInfo.getString("key")));
            //Si la vista de ver cuentas esta activa
            AccountsPresenter presenter = ViewPresenterManager
                    .getPresenter(AccountsPresenter.class);
            if (presenter != null)
                presenter.loadAccounts();
            //Si la vista de ver notificaciones está activa
            NotificationsPresenter notifPresenter = ViewPresenterManager
                    .getPresenter(NotificationsPresenter.class);
            if (notifPresenter != null)
                notifPresenter.addNewAccountNotificationUpdate(notif);
            notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());*/
        }
    }

}
