package com.elfec.ssc.messaging;

import rx.Observable;
import rx_gcm.GcmReceiverUIBackground;
import rx_gcm.Message;

/**
 * Receives the notification for user showing
 */
public class GcmNotificationBgReceiver implements GcmReceiverUIBackground {
    @Override
    public void onNotification(Observable<Message> oMessage) {
        oMessage.subscribe(message -> {

        });
    }
}