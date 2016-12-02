package com.elfec.ssc.messaging;

import com.elfec.ssc.messaging.handlers.GcmHandlerFactory;
import com.elfec.ssc.messaging.handlers.INotificationHandler;

import rx.Observable;
import rx_gcm.GcmReceiverData;
import rx_gcm.Message;

/**
 * Class for gcm notifications handling
 */
public class GcmNotificationReceiver implements GcmReceiverData {

    public static final String NOTIFICATION_TYPE_KEY = "type";

    @Override
    public Observable<Message> onNotification(Observable<Message> oMessage) {
        return oMessage.map(message -> {
            String type = message.payload().getString(NOTIFICATION_TYPE_KEY);
            if (type != null) {
                INotificationHandler handler = GcmHandlerFactory.create(type);
                //if (handler != null)
                    //handler.handleNotification(message.payload());
            }
            return message;
        });
    }

}
