package com.elfec.ssc.business_logic;

import com.elfec.ssc.local_storage.UsageStorage;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.web_services.UsageService;
import com.elfec.ssc.web_services.SscTokenRequester;

import java.util.List;

import rx.Observable;

/**
 * Created by drodriguez on 02/12/2016.
 * Usage manager
 */

public class UsageManager {
    /**
     * Retrieves the usages of an account from web services and saves them for further uses
     * @param nus account's NUS
     * @return observable of usage list
     */
    public static Observable<List<Usage>> syncUsages(String nus) {
        return getUsagesFromWs(nus)
                .flatMap(usages -> new UsageStorage()
                        .saveUsages(nus, usages));
    }

    /**
     * Gets the usages of an account via web services
     * @param nus account's NUS
     * @return observable of usage list
     */
    public static Observable<List<Usage>> getUsagesFromWs(String nus){
        return new SscTokenRequester().getSscToken()
                .flatMap(sscToken ->
                        new UsageService(sscToken).getUsages(nus));
    }
}
