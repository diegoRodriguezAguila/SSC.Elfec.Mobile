package com.elfec.ssc.messaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.HtmlCompat;
import com.elfec.ssc.messaging.handlers.GcmHandlerFactory;
import com.elfec.ssc.messaging.handlers.INotificationHandler;

import rx.Observable;
import rx_gcm.GcmReceiverData;
import rx_gcm.Message;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Class for gcm notifications handling
 */
public class GcmNotificationReceiver implements GcmReceiverData {
    private static final String TAG = "GcmNotification";

    @Override
    public Observable<Message> onNotification(Observable<Message> oMessage) {
        return oMessage.map(message -> {
            Bundle payload = message.payload();
            Log.d(TAG, "Recibido mensaje: " + payload.toString());
            INotificationHandler gcmHandler = GcmHandlerFactory.create(payload.getString("key"));
            if (gcmHandler == null) return message;
            NotificationManager notifManager = getNotificationManager(message);
            NotificationCompat.Builder builder = getBuilder(message);
            if (gcmHandler.getActivityClass() != null) {
                builder.setContentIntent(getPendingIntent(message, gcmHandler));
            }
            gcmHandler.handleNotification(payload, notifManager, builder);
            return message;
        });
    }

    @NonNull
    private NotificationCompat.Builder getBuilder(Message message) {
        CharSequence content = HtmlCompat.fromHtml(message.payload()
                .getString("message"));
        return new NotificationCompat.Builder(
                message.application())
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setColor(ContextCompat.getColor(message.application(), R.color.ssc_elfec_color))
                .setContentTitle(HtmlCompat.fromHtml(message.payload().getString("title")))
                .setContentText(content)
                .setSmallIcon(R.drawable.elfec_notification);
    }

    private NotificationManager getNotificationManager(Message message) {
        return (NotificationManager)
                message.application().getSystemService(NOTIFICATION_SERVICE);
    }

    private PendingIntent getPendingIntent(Message message, INotificationHandler gcmHandler) {
        Intent notificationIntent = new Intent(message.application(),
                gcmHandler.getActivityClass());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(
                message.application(), 0, notificationIntent, 0);
    }

}
