package com.elfec.ssc.messaging;


import rx.Observable;
import rx_gcm.GcmRefreshTokenReceiver;
import rx_gcm.TokenUpdate;

/**
 * Class that refreshes the token when necessary
 */
public class RefreshTokenReceiver implements GcmRefreshTokenReceiver {

    @Override public void onTokenReceive(Observable<TokenUpdate> oTokenUpdate) {
        /*oTokenUpdate.flatMap((tokenUpdate ->
                new DeviceManager().updateGcmToken(tokenUpdate.getToken())))
        .subscribe(tokenUpdate -> {}, error -> {});*/
    }

}