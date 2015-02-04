package com.elfec.ssc.businesslogic;

import org.joda.time.DateTime;

import com.activeandroid.query.Delete;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;

/**
 * Maneja las distintas operaciones de Notificaciones
 * @author Diego
 *
 */
public class ElfecNotificationManager {


	/**
	 * Guarda una notificación, ejecutando el proceso en un hilo
	 * @param title
	 * @param content
	 * @param type
	 */
	public static void SaveNotification(final String title, final String content, final NotificationType type, final NotificationKey key)
    {
    	Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				Notification notification=new Notification(title,content,type, key);
				notification.setInsertDate(DateTime.now());
				notification.save();
			}
		});
    	thread.start();
    }

	/**
	 * Elimina todas las notificaciones del tipo designado
	 * @param type
	 */
	public static void removeAllNotifications(NotificationType type)
	{
		new Delete().from(Notification.class).where("Type=?",type.toShort()).execute();
	}


}
