package com.elfec.ssc.messaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;

import com.elfec.ssc.R;
import com.elfec.ssc.messaging.handlers.GcmHandlerFactory;
import com.elfec.ssc.messaging.handlers.INotificationHandler;

/**
 * Se encarga de procesar el mensaje recibido por el GCMBroadcastReceiver
 *
 * @author Zuki
 */
public class GcmMessageService extends IntentService {
    private static final String TAG = "GcmMessage";

    public GcmMessageService() {
        super("GcmMessageService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, intent.getExtras().toString());
        Bundle messageInfo = intent.getExtras();
        INotificationHandler gcmHandler = GcmHandlerFactory.create(messageInfo
                .getString("key"));
        if (gcmHandler != null) {
            NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getApplicationContext());
            if (gcmHandler.getActivityClass() != null) {
                Intent notificationIntent = new Intent(getApplicationContext(),
                        gcmHandler.getActivityClass());
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pending = PendingIntent.getActivity(
                        getApplicationContext(), 0, notificationIntent, 0);
                builder.setContentIntent(pending);
            }
            gcmHandler
                    .handleNotification(
                            messageInfo,
                            notifManager,
                            builder.setSound(
                                    RingtoneManager
                                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                    .setColor(
                                            ContextCompat.getColor(this,
                                                    R.color.ssc_elfec_color))
                                    .setContentTitle(
                                            Html.fromHtml(messageInfo
                                                    .getString("title")))
                                    .setContentText(
                                            Html.fromHtml(messageInfo
                                                    .getString("message")))
                                    .setSmallIcon(R.drawable.elfec_notification));
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

}