package com.elfec.ssc.view.gcmservices;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.elfec.ssc.R;

/**
 * Se encarga de procesar el mensaje recibido por el GCMBroadcastReceiver
 * @author Zuki
 *
 */
public class GcmMessageHandler extends IntentService {

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle messageInfo = intent.getExtras();
		IGCMHandler gcmHandler = GCMHandlerFactory.getGCMHandler(messageInfo.getString("key"));
		if(gcmHandler!=null)
		{
			NotificationManager notifManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(getApplicationContext(), gcmHandler.getActivityClass());
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);   
            PendingIntent pending = PendingIntent.getActivity(getApplicationContext(),0, notificationIntent,0);         
            gcmHandler.handleGCMessage(messageInfo, notifManager, 
            		(new NotificationCompat.Builder(getApplicationContext()))
            							.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
							   		    .setContentIntent(pending)
							   		    .setContentTitle(messageInfo.getString("title"))
										.setContentText(messageInfo.getString("message"))
							   		    .setSmallIcon(R.drawable.elfec_notification));
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

}