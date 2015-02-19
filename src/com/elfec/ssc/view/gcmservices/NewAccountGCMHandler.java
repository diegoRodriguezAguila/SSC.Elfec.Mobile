package com.elfec.ssc.view.gcmservices;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.ElfecNotificationManager;
import com.elfec.ssc.helpers.JsonToAccountConverter;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.ViewNotificationsPresenter;
import com.elfec.ssc.view.ViewAccounts;

/**
 * Maneja las notificaciones GCM de nuevas cuentas
 * @author Diego
 *
 */
public class NewAccountGCMHandler implements IGCMHandler {

	private final int NOTIF_ID = 1;
	@Override
	public Class<? extends Activity> getActivityClass() {
		return ViewAccounts.class;
	}

	@Override
	public void handleGCMessage(Bundle messageInfo,	NotificationManager notifManager, NotificationCompat.Builder builder) {
		Client ownerClient = Client.getClientByGmail(messageInfo.getString("gmail"));
		if(ownerClient != null && ownerClient.getStatus()==ClientStatus.ACTIVE)
		{
			try {
				ElfecAccountsManager.registerAccount(JsonToAccountConverter.convert(new JSONObject(messageInfo.getString("account"))));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Notification notif = ElfecNotificationManager.SaveNotification(messageInfo.getString("title"), messageInfo.getString("message"),
					NotificationType.get(Short.parseShort(messageInfo.getString("type"))), NotificationKey.get(messageInfo.getString("key")));
			//Si la vista de ver cuentas esta activa
			ViewAccountsPresenter presenter = ViewPresenterManager
					.getPresenter(ViewAccountsPresenter.class);
			if (presenter != null)
				presenter.gatherAccounts();
			//Si la vista de ver notificaciones está activa
			ViewNotificationsPresenter notifPresenter = ViewPresenterManager
					.getPresenter(ViewNotificationsPresenter.class);
			if (notifPresenter != null)
				notifPresenter.addNewAccountNotificationUpdate(notif);
			notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
		}
	}

}
