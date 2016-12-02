package com.elfec.ssc.local_storage;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.model.Client;

import rx.Observable;

/**
 * Created by Diego on 21/8/2016.
 * local storage for clients
 */
public class ClientStorage {
    public static final String CLIENT_BOOK = "client.book";
    private static final String ACTIVE_CLIENT_KEY = "active_client";

    /**
     * Saves the current active client to the database
     *
     * @param client to save
     * @return Observable of client
     */
    public Observable<Client> saveClient(Client client) {
        return RxPaper.book(CLIENT_BOOK).write(ACTIVE_CLIENT_KEY, client);
    }

    /**
     * Gets the current active client from the database
     *
     * @return Observable of client
     */
    public Observable<Client> getClient() {
        return RxPaper.book(CLIENT_BOOK).read(ACTIVE_CLIENT_KEY);
    }
}
