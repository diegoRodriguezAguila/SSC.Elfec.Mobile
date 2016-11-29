package com.elfec.ssc.gcmservices;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.elfec.ssc.businesslogic.ContactsManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.presenter.ContactPresenter;
import com.elfec.ssc.view.Contacts;
/**
 * Maneja las notificaciones de actualización de contactos
 * @author drodriguez
 *
 */
public class ContactsUpdateGCMHandler implements IGCMHandler {

	private final int NOTIF_ID = 4;
	@Override
	public void handleGCMessage(Bundle messageInfo,
			NotificationManager notifManager, Builder builder) {
		ContactsManager.updateContactData(messageInfo.getString("phone"), messageInfo.getString("address"), 
				messageInfo.getString("email"), messageInfo.getString("web_page"), messageInfo.getString("facebook"),
						messageInfo.getString("facebook_id"));
		//Si la vista de contactos está activa
		ContactPresenter presenter = ViewPresenterManager.getPresenter(ContactPresenter.class);
		if (presenter != null)
			presenter.setDefaultData();
		notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return Contacts.class;
	}

}
