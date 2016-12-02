package com.elfec.ssc.business_logic;

import android.app.Application;

import com.elfec.ssc.helpers.RxGcmHelper;
import com.elfec.ssc.messaging.GcmNotificationBgReceiver;
import com.elfec.ssc.messaging.GcmNotificationReceiver;
import com.elfec.ssc.messaging.RefreshTokenReceiver;
import com.elfec.ssc.security.AppPreferences;

import rx.Observable;
import rx_gcm.internal.RxGcm;

/**
 * Created by drodriguez on 02/12/2016.
 * device stuff manager
 */

public class DeviceManager {
    /**
     * Gets the gcm token and synchronizes it with the api server
     * @return void Observable
     */
    public Observable<Void> syncGcmToken(){
        return getGcmToken().flatMap(this::registerGcmToken)
                .doOnCompleted(() -> RxGcm.Notifications
                        .onRefreshToken(RefreshTokenReceiver.class));
    }

    /**
     * Gets the Gcm token locally or remotely
     * @return observable of token
     */
    public static Observable<String> getGcmToken(){
        return RxGcmHelper.register((Application) AppPreferences.getApplicationContext(),
                GcmNotificationReceiver.class,
                GcmNotificationBgReceiver.class);
    }

    /**
     * Registers the specified gcm token for this device in the server, if the
     * registration fails because the token was already registered for this device,
     * it sends an update token request.
     * @param token token
     * @return observable
     */
    public Observable<Void> registerGcmToken(String token) {
        return Observable.empty();
    }

}
