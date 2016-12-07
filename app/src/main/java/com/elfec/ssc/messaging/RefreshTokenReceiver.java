package com.elfec.ssc.messaging;


import com.elfec.ssc.business_logic.DeviceManager;
import com.elfec.ssc.security.AppPreferences;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx_gcm.GcmRefreshTokenReceiver;
import rx_gcm.TokenUpdate;

/**
 * Class that refreshes the token when necessary
 */
public class RefreshTokenReceiver implements GcmRefreshTokenReceiver {

    @Override
    public void onTokenReceive(Observable<TokenUpdate> oTokenUpdate) {
        oTokenUpdate
                .flatMap(tokenUpdate -> {
                    AppPreferences.instance().setHasToSendGcmToken(true);
                    return new DeviceManager().registerGcmToken(tokenUpdate.getToken());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(success -> {
                }, error -> {
                });
    }

}