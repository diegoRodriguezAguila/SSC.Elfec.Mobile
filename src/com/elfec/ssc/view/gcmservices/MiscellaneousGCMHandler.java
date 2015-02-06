package com.elfec.ssc.view.gcmservices;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Maneja aquellas notificaciones con el key miscellaneous, simplemente las muestra, no las guarda
 * @author drodriguez
 *
 */
public class MiscellaneousGCMHandler implements IGCMHandler {

	private final int NOTIF_ID = 5;
	@Override
	public void handleGCMessage(Bundle messageInfo,
			NotificationManager notifManager, Builder builder) {
		notifManager.notify(NOTIF_ID, builder.setAutoCancel(true).build());
	}

	@Override
	public Class<? extends Activity> getActivityClass() {
		return null;
	}

}
