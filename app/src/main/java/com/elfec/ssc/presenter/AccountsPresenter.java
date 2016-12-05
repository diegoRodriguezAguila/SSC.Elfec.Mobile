package com.elfec.ssc.presenter;

import android.content.Context;
import android.os.Looper;

import com.elfec.ssc.business_logic.AccountManager;
import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.exceptions.OutdatedAppException;
import com.elfec.ssc.presenter.views.IAccountsView;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;
import com.elfec.ssc.web_services.AccountService;
import com.elfec.ssc.web_services.SscTokenRequester;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountsPresenter extends BasePresenter<IAccountsView> {

    private SscTokenRequester mSscTokenRequester;
    private final String mImei;
    private String mGmail;

    public AccountsPresenter(IAccountsView view) {
        super(view);
        Context context = AppPreferences.getApplicationContext();
        mSscTokenRequester = new SscTokenRequester();
        mImei = new CredentialManager(context)
                .getDeviceIdentifier();
    }

    /**
     * Invoca a los webservices necesarios para eliminar una cuenta
     *
     * @param nus nus
     */
    public void removeAccount(final String nus) {

        new Thread(() -> {
            Looper.prepare();
            mSscTokenRequester.getTokenAsync(wsTokenResult -> {
                final Client client = null;//TODO current client Client.getClientByGmail(messageInfo
                // .getString("gmail"));
                new AccountService(wsTokenResult.getResult()).removeAccount(client.getGmail(),
                        nus, mImei, result -> {
                            if (result.getResult()) {
                                AccountManager.deleteAccount(client.getGmail(), nus);
                                loadAccounts();
                                mView.showAccountDeleted();
                                mView.hideWaiting();
                            } else {
                                mView.hideWaiting();
                                mView.onError(result.getErrors().get(0));
                            }
                        });
            });

            Looper.loop();
        }).start();
    }

    /**
     * Obtiene las cuentas del cliente remotamente, si no hay conexión obtiene la
     * versión local
     */
    public void loadAccounts() {
        mView.showWaiting();
        cancelSubscription();
        mSubscription = ClientManager.activeClient()
                .flatMap(client -> {
                    mGmail = client.getGmail();
                    return new AccountStorage().getAccounts(mGmail);
                })
                .flatMap(accounts -> {
                    mView.hideWaiting();
                    mView.onLoaded(accounts);
                    return AccountManager.syncAccounts(mGmail);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {
                    mView.hideWaiting();
                    mView.onLoaded(accounts);
                }, e -> {
                    mView.hideWaiting();
                    if (e instanceof OutdatedAppException) {
                        mView.onError(e);
                        mView.onLoaded(null);
                    }
                });
    }

    /**
     * Obtiene las cuentas del cliente remotamente
     */
    public void refreshAccounts() {
        mView.showWaiting();
        cancelSubscription();
        mSubscription = AccountManager.syncAccounts(mGmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {
                    mView.hideWaiting();
                    mView.onLoaded(accounts);
                }, e -> {
                    mView.hideWaiting();
                    mView.onError(e);
                });
    }
}
