package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.presenter.AccountsPresenter;
import com.elfec.ssc.presenter.NotificationsPresenter;
import com.elfec.ssc.view.AccountsActivity;

/**
 * Maneja las notificaciones de eliminación de cuentas
 *
 * @author drodriguez
 */
public class AccountDeletedGCMHandler implements INotificationHandler {

    private static final int NOTIF_ID = 2;

    @Override
    public Class<? extends Activity> getActivityClass() {
        return AccountsActivity.class;
    }

    @Override
    public void handleNotification(Bundle messageInfo, android.app.NotificationManager notifManager, NotificationCompat.Builder builder) {
        //TODO replace Client ownerClient = Client.getClientByGmail(messageInfo.getString("gmail"));
        Client ownerClient = null;
        if (ownerClient != null && ownerClient.getStatus() == ClientStatus.ACTIVE) {
            //boolean res = AccountManager.deleteAccount(ownerClient.getGmail(), messageInfo.getString("nus"));
            if (true) {
                /*Notification notif =  NotificationManager.SaveNotification(messageInfo.getString
                        ("title"), messageInfo.getString("message"),
                        NotificationType.get(Short.parseShort(messageInfo.getString("type"))), NotificationKey.get(messageInfo.getString("key")));
                */
                //Si la vista de ver cuentas esta activa
                AccountsPresenter presenter = ViewPresenterManager
                        .getPresenter(AccountsPresenter.class);
                if (presenter != null)
                    presenter.loadAccounts();
                //Si la vista de ver notificaciones está activa
                NotificationsPresenter notifPresenter = ViewPresenterManager
                        .getPresenter(NotificationsPresenter.class);
                /*if (notifPresenter != null)
                    notifPresenter.addNewAccountNotificationUpdate(notif);*/
                notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
            }
        }
    }

}
