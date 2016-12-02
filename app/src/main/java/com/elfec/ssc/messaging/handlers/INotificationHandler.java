package com.elfec.ssc.messaging.handlers;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Define una interfaz para los GCM handlers
 * @author Diego
 *
 */
public interface INotificationHandler {

	/**
	 * Se encarga de manejar el mensaje de forma adecuada
	 * @param messageInfo
	 * @param notifManager
	 * @param builder
	 */
	public void handleNotification(Bundle messageInfo, NotificationManager notifManager, Builder builder);
	/**
	 * Obtiene el tipo de actividad que se iniciará al presionar en una notificación
	 * en caso de tener una notificación que debe llevar a una vista
	 * @return la clase de actividad que se mostrará
	 */
	public Class<? extends Activity> getActivityClass();
}
