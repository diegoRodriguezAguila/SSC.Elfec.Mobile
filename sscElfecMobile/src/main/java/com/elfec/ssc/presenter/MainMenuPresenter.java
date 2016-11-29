package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.presenter.views.IMainMenu;
import com.elfec.ssc.security.AppPreferences;


public class MainMenuPresenter {

    private IMainMenu view;

    public MainMenuPresenter(IMainMenu view) {
        this.view = view;
    }

    /**
     * Verifica si es que ya se definió el cliente activo para obtener sus cuentas caso contrario
     * se le advierte al usuario
     */
    public void verifyAccountsRequirements() {
        if (AppPreferences.instance().hasOneGmailAccount())
            view.goToViewAccounts();
        else
            view.warnUserHasNoAccounts();
    }

    public void handlePickedGmailAccount(final String gmail) {
        ThreadMutex.instance("ActiveClient").setBusy();
        view.setCurrentClient(gmail);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ClientManager.registerActiveClient(gmail);
                AppPreferences.instance().setHasOneGmailAccount();
                ThreadMutex.instance("ActiveClient").setFree();
            }
        });
        thread.start();
        view.goToViewAccounts();
    }

    /**
     * obtiene el cliente actual
     */
    public void loadCurrentClient() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = Client.getActiveClient();
                view.setCurrentClient(client == null ? null : client.getGmail());
            }
        });
        thread.start();
    }
}
