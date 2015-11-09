package com.elfec.ssc.gcmservices;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Define una interfaz para los GCM handlers
 * @author Diego
 *
 */
public interface IGCMHandler {

	/**
	 * Se encarga de manejar el mensaje de forma adecuada
	 * @param messageInfo
	 * @param notifManager
	 * @param builder
	 */
	public void handleGCMessage(Bundle messageInfo, NotificationManager notifManager, Builder builder);
	/**
	 * Obtiene el tipo de actividad que se iniciar� al presionar en una notificaci�n
	 * en caso de tener una notificaci�n que debe llevar a una vista
	 * @return la clase de actividad que se mostrar�
	 */
	public Class<? extends Activity> getActivityClass();
}
