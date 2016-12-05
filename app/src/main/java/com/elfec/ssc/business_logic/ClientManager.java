package com.elfec.ssc.business_logic;

import com.elfec.ssc.local_storage.ClientStorage;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.security.AppPreferences;

import rx.Observable;

/**
 * Se encarga de las distintas operaciones relacionadas con los clientes
 *
 * @author Diego
 */
public class ClientManager {

    /**
     * Saves the current in the database and marks the flag HasOneGmailAccount as true
     *
     * @param client client
     * @return observable of client
     */
    public static Observable<Client> registerActiveClient(Client client) {
        return new ClientStorage().saveClient(client)
                .map(c -> {
                    AppPreferences.instance().setAppAlreadyUsed().setHasOneGmailAccount();
                    return c;
                });
    }

    /**
     * Gets the current active client
     *
     * @return observable of client
     */
    public static Observable<Client> activeClient() {
        return new ClientStorage().getClient();
    }
}
