package com.elfec.ssc.local_storage;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.model.Usage;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by drodriguez on 01/12/2016.
 * UsageStorage
 */

public class UsageStorage {

    private static final String USAGES_BOOK = "usages.book";

    /**
     * Saves the usages to the database with all its subclasses, it doesn't execute
     * inmediately,
     * it creates an observable to be execute in the future
     *
     * @param nus    Account's NUS
     * @param usages to save
     * @return Observable of  usages
     */
    public Observable<List<Usage>> saveUsages(String nus, List<Usage> usages) {
        return RxPaper.book(USAGES_BOOK)
                .write(nus, usages);
    }

    /**
     * Saves a usage to the database
     *
     * @param nus   Account's NUS
     * @param usage to save
     * @return Observable of usage
     */
    public Observable<Usage> saveUsage(String nus, Usage usage) {
        return getUsages(nus)
                .flatMap(usages -> {
                    if (usages == null)
                        usages = new ArrayList<>();
                    if (!usages.contains(usage))
                        usages.add(usage);
                    return saveUsages(nus, usages);
                }).map(m -> usage);
    }

    /**
     * Retrieves usages from database
     *
     * @param nus Account's NUS
     * @return Observable of a Usages
     */
    public Observable<List<Usage>> getUsages(String nus) {
        return RxPaper.book(USAGES_BOOK).read(nus);
    }
}
