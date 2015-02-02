package com.elfec.ssc.businesslogic;

import com.activeandroid.query.Delete;
import com.elfec.ssc.model.Notification;



public class ElfecNotificationManager {

	public static void removeAllNotifications(short type)
	{
		new Delete().from(Notification.class).where("Type=?",type).execute();
	}

}
