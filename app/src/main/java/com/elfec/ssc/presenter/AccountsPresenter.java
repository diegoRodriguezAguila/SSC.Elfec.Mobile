package com.elfec.ssc.presenter;

import android.content.Context;
import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.ElfecAccountsManager;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.helpers.utils.ErrorVerifierHelper;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.model.gcmservices.GcmTokenRequester;
import com.elfec.ssc.presenter.views.IAccountsView;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;
import com.elfec.ssc.web_services.AccountService;
import com.elfec.ssc.web_services.SscTokenRequester;

import java.util.List;

public class AccountsPresenter extends BasePresenter<IAccountsView> {

    private boolean mIsLoadingAccounts;
    private boolean mIsRefreshing;
    private GcmTokenRequester mGcmTokenRequester;
    private SscTokenRequester mSscTokenRequester;
    private final String mImei;

    public AccountsPresenter(IAccountsView view) {
        super(view);
        mIsLoadingAccounts = false;
        mIsRefreshing = false;
        Context context = AppPreferences.getApplicationContext();
        mGcmTokenRequester = new GcmTokenRequester(context);
        mSscTokenRequester = new SscTokenRequester();
        mImei = new CredentialManager(context)
                .getDeviceIdentifier();
    }

    /**
     * Invoca a los webservices necesarios para eliminar una cuenta
     * @param nus nus
     */
    public void removeAccount(final String nus) {
        new Thread(() -> {
            Looper.prepare();
            mSscTokenRequester.getTokenAsync(wsTokenResult -> {
                final Client client =null;//TODO current client Client.getClientByGmail(messageInfo
                // .getString("gmail"));
                new AccountService(wsTokenResult.getResult()).removeAccount(client.getGmail(),
                        nus, mImei, result -> {
                            if (result.getResult()) {
                                ElfecAccountsManager.deleteAccount(client.getGmail(), nus);
                                loadAccounts(true);
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
     * @param preLoad si es verdadero se cargan las cuentas locales antes
     *                de intentar realizar una llamada remota
    */
    public void loadAccounts(final boolean preLoad) {
        ThreadMutex.instance("ActiveClient").addOnThreadReleasedEvent(() -> {
            final Client client =null;//TODO current client Client.getClientByGmail(messageInfo
            // .getString("gmail"));
            if(preLoad)
                loadAccountsLocally(client);
            new Thread(() -> {
                Looper.prepare();
                mGcmTokenRequester.getTokenAsync(new GcmTokenCallback() {
                    @Override
                    public void onGcmTokenReceived(String gcmToken) {
                        loadAccountsRemotely(client, gcmToken);
                    }

                    @Override
                    public void onGcmErrors(List<Exception> errors) {
                        mView.hideWaiting();
                        mView.onError(errors.get(0));
                        mView.onLoaded(null);
                    }
                });
                Looper.loop();
            }).start();
        });
    }

    /**
     * Obtiene las cuentas del cliente remotamente, si no hay conexión obtiene la
     * versión local
     */
    public void loadAccounts() {
        /*ThreadMutex.instance("ActiveClient").addOnThreadReleasedEvent(() -> {
            final Client client = Client.getActiveClient();
            if(preLoad)
                loadAccountsLocally(client);
            new Thread(() -> {
                Looper.prepare();
                mGcmTokenRequester.getTokenAsync(new GcmTokenCallback() {
                    @Override
                    public void onGcmTokenReceived(String gcmToken) {
                        loadAccountsRemotely(client, gcmToken);
                    }

                    @Override
                    public void onGcmErrors(List<Exception> errors) {
                        mView.hideWaiting();
                        mView.onError(errors.get(0));
                        mView.onLoaded(null);
                    }
                });
                Looper.loop();
            }).start();
        });*/
    }

    /**
     * Actualiza las cuentas a partir de los web services
     */
    public void refreshAccountsRemotely() {
        mIsRefreshing = true;
        loadAccounts(false);
    }

    /**
     * Invoca a las clases necesarias para obtener las cuentas del cliente via web services
     *
     * @param client cliente
     */
    private void loadAccountsRemotely(final Client client, final String gcmToken) {
        if (!mIsLoadingAccounts) {
            mIsLoadingAccounts = true;
            mSscTokenRequester.getTokenAsync(wsTokenResult ->
                    new AccountService(wsTokenResult.getResult()).getAllAccounts(client.getGmail(),
                    Build.BRAND, Build.MODEL, mImei, gcmToken,
                            result -> new Thread(() -> {
                                if (result.getErrors().size() == 0) {
                                    final List<Account> accounts =
                                            ClientManager.registerClientAccounts(result.getResult());
                                    mView.hideWaiting();
                                    mView.onLoaded(accounts);
                                } else {
                                    mView.hideWaiting();
                                    if (mIsRefreshing || ErrorVerifierHelper.isOutdatedApp(result
                                            .getErrors()))
                                        mView.onError(result.getErrors().get(0));
                                }
                                mIsLoadingAccounts = false;
                                mIsRefreshing = false;
                            }).start()));
        }
    }

    /**
     * Obtiene las cuentas locales del cliente
     *
     * @param client cliente
     */
    public void loadAccountsLocally(Client client) {
        List<Account> activeAccounts = client.getActiveAccounts();
        mView.hideWaiting();
        mView.onLoaded(activeAccounts);
    }

}
