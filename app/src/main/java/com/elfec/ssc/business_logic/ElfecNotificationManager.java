package com.elfec.ssc.business_logic;

import com.activeandroid.query.Delete;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;

import org.joda.time.DateTime;

/**
 * Maneja las distintas operaciones de Notificaciones
 * @author Diego
 *
 */
public class ElfecNotificationManager {


	/**
	 * Guarda una notificación
	 * @param title
	 * @param content
	 * @param type
	 * @return Notification
	 */
	public static Notification SaveNotification(final String title, final String content, final NotificationType type, final NotificationKey key)
    {
		Notification notification=new Notification(title,content,type, key);
		notification.setInsertDate(DateTime.now());
		notification.save();
		return notification;
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
