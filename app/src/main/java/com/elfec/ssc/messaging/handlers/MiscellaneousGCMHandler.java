package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Maneja aquellas notificaciones con el key miscellaneous, simplemente las muestra, no las guarda
 * @author drodriguez
 *
 */
public class MiscellaneousGcmHandler implements INotificationHandler {

	private static final int NOTIF_ID = 5;
	@Override
	public void handleNotification(Bundle message,
								   NotificationManager notifManager, Builder builder) {
		notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return null;
	}

}
