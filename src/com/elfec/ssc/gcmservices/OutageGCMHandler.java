package com.elfec.ssc.gcmservices;

import com.elfec.ssc.businesslogic.ElfecNotificationManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;
import com.elfec.ssc.presenter.ViewNotificationsPresenter;
import com.elfec.ssc.view.ViewNotifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Maneja todos los tipos de notificaciones relacionadas a cortes
 * @author drodriguez
 *
 */
public class OutageGCMHandler implements IGCMHandler {

	private final int NOTIF_ID = 3;
	@Override
	public void handleGCMessage(Bundle messageInfo,
			NotificationManager notifManager, Builder builder) {
		Client ownerClient = Client.getClientByGmail(messageInfo.getString("gmail"));
		if(ownerClient != null && ownerClient.getStatus()==ClientStatus.ACTIVE)
		{
			Notification notif = ElfecNotificationManager.SaveNotification(messageInfo.getString("title"), messageInfo.getString("message"),
					NotificationType.get(Short.parseShort(messageInfo.getString("type"))), NotificationKey.get(messageInfo.getString("key")));
			//Si la vista de ver notificaciones está activa
			ViewNotificationsPresenter notifPresenter = ViewPresenterManager
					.getPresenter(ViewNotificationsPresenter.class);
			if (notifPresenter != null)
				notifPresenter.addNewOutageNotificationUpdate(notif);
			notifManager.notify(NOTIF_ID, builder.build());
		}
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return ViewNotifications.class;
	}

}
