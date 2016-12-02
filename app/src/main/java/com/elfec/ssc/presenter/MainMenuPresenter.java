package com.elfec.ssc.presenter;

import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.presenter.views.IMainMenuView;
import com.elfec.ssc.security.AppPreferences;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainMenuPresenter extends BasePresenter<IMainMenuView> {

    public MainMenuPresenter(IMainMenuView view) {
        super(view);
    }

    /**
     * Verifica si es que ya se definió el cliente activo para obtener sus cuentas caso contrario
     * se le advierte al usuario
     */
    public void verifyAccountsRequirements() {
        if (AppPreferences.instance().hasOneGmailAccount())
            mView.goToViewAccounts();
        else mView.warnUserHasNoAccounts();
    }

    public void handlePickedGmailAccount(final String gmail) {
        mView.setCurrentClient(gmail);
        ClientManager.registerActiveClient(new Client(gmail, ClientStatus.ACTIVE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(client -> {
                    mView.goToViewAccounts();
                }, e -> {
                });
    }

    /**
     * obtiene el cliente actual
     */
    public void loadCurrentClient() {
        ClientManager.activeClient().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(client -> {
                    mView.setCurrentClient(client == null ? null : client.getGmail());
                }, e -> {
                });
    }
}
