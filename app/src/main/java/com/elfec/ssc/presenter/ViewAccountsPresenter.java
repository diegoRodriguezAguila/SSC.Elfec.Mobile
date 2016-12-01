package com.elfec.ssc.presenter;

import android.content.Context;
import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.ElfecAccountsManager;
import com.elfec.ssc.web_services.AccountWS;
import com.elfec.ssc.web_services.SscTokenRequester;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.helpers.utils.ErrorVerifierHelper;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.SscTokenReceivedCallback;
import com.elfec.ssc.model.gcmservices.GcmTokenRequester;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;

import java.util.List;

public class ViewAccountsPresenter {

    private IViewAccounts view;
    private boolean mIsLoadingAccounts;
    private boolean mIsRefreshing;
    private GcmTokenRequester mGcmTokenRequester;
    private SscTokenRequester mSscTokenRequester;
    private final String mImei;

    public ViewAccountsPresenter(IViewAccounts view) {
        this.view = view;
        mIsLoadingAccounts = false;
        mIsRefreshing = false;
        Context context = AppPreferences.getApplicationContext();
        mGcmTokenRequester = new GcmTokenRequester(context);
        mSscTokenRequester = new SscTokenRequester(context);
        mImei = new CredentialManager(context)
                .getDeviceIdentifier();
    }

    /**
     * Invoca a los webservices necesarios para eliminar una cuenta
     * @param nus nus
     */
    public void removeAccount(final String nus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mSscTokenRequester.getTokenAsync(new SscTokenReceivedCallback() {
                    @Override
                    public void onSscTokenReceived(WSResponse<SscToken> wsTokenResult) {
                        final Client client = Client.getActiveClient();
                        new AccountWS(wsTokenResult.getResult()).removeAccount(client.getGmail(),
                                nus, mImei, new IWSFinishEvent<Boolean>() {
                                    @Override
                                    public void executeOnFinished(WSResponse<Boolean> result) {
                                        if (result.getResult()) {
                                            ElfecAccountsManager.deleteAccount(client.getGmail(), nus);
                                            loadAccounts(true);
                                            view.showAccountDeleted();
                                            view.hideWaiting();
                                        } else {
                                            view.hideWaiting();
                                            view.displayErrors(result.getErrors());
                                        }
                                    }
                                });
                    }
                });

                Looper.loop();
            }
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
                        view.hideWaiting();
                        view.showViewAccountsErrors(errors);
                        view.showAccounts(null);
                    }
                });
                Looper.loop();
            }).start();
        });
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
                    new AccountWS(wsTokenResult.getResult()).getAllAccounts(client.getGmail(),
                    Build.BRAND, Build.MODEL, mImei, gcmToken,
                            result -> new Thread(() -> {
                                if (result.getErrors().size() == 0) {
                                    final List<Account> accounts =
                                            ClientManager.registerClientAccounts(result.getResult());
                                    view.hideWaiting();
                                    view.showAccounts(accounts);
                                } else {
                                    view.hideWaiting();
                                    if (mIsRefreshing || ErrorVerifierHelper.isOutdatedApp(result
                                            .getErrors()))
                                        view.showViewAccountsErrors(result.getErrors());
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
        view.hideWaiting();
        view.showAccounts(activeAccounts);
    }

}
