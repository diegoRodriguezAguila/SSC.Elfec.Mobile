package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.view.NotificationsActivity;

/**
 * Maneja todos los tipos de notificaciones relacionadas a cortes
 * @author drodriguez
 *
 */
public class OutageGCMHandler implements INotificationHandler {

	private final int NOTIF_ID = 3;
	@Override
	public void handleNotification(Bundle messageInfo,
								   android.app.NotificationManager notifManager, Builder builder) {
		Client ownerClient =null;//TODO current client Client.getClientByGmail(messageInfo
		// .getString("gmail"));
		if(ownerClient != null && ownerClient.getStatus()==ClientStatus.ACTIVE)
		{
			/*Notification notif = NotificationManager.SaveNotification(messageInfo.getString
				("title"), messageInfo.getString("message"),
					NotificationType.get(Short.parseShort(messageInfo.getString("type"))), NotificationKey.get(messageInfo.getString("key")));
			//Si la vista de ver notificaciones está activa
			NotificationsPresenter notifPresenter = ViewPresenterManager
					.getPresenter(NotificationsPresenter.class);
			if (notifPresenter != null)
				notifPresenter.addNewOutageNotificationUpdate(notif);
			notifManager.notify(NOTIF_ID, builder.build());*/
		}
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return NotificationsActivity.class;
	}

}
