package com.elfec.ssc.presenter;

import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.businesslogic.webservices.SscTokenRequester;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.helpers.threading.ThreadMutex;
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

import java.util.List;

public class ViewAccountsPresenter {

    private IViewAccounts view;
    private boolean mIsLoadingAccounts;
    private boolean mIsRefreshing;
    private GcmTokenRequester mGcmTokenRequester;
    private SscTokenRequester mSscTokenRequester;

    public ViewAccountsPresenter(IViewAccounts view) {
        this.view = view;
        mIsLoadingAccounts = false;
        mIsRefreshing = false;
        mGcmTokenRequester = new GcmTokenRequester(AppPreferences.getApplicationContext());
        mSscTokenRequester = new SscTokenRequester(AppPreferences.getApplicationContext());
    }

    /**
     * Invoca a los webservices necesarios para eliminar una cuenta
     * @param nus
     */
    public void removeAccount(final String nus) {
        final String imei = view.getImei();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mSscTokenRequester.getTokenAsync(new SscTokenReceivedCallback() {
                    @Override
                    public void onSscTokenReceived(WSResponse<SscToken> wsTokenResult) {
                        final Client client = Client.getActiveClient();
                        new AccountWS(wsTokenResult.getResult()).removeAccount(client.getGmail(),
                                nus, imei, new IWSFinishEvent<Boolean>() {
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
     * Obtiene las cuentas del cliente remotamente, si no hay conexi�n obtiene la
     * versi�n local
     * @param preLoad si es verdadero se cargan las cuentas locales antes
     *                de intentar realizar una llamada remota
     */
    public void loadAccounts(final boolean preLoad) {
        ThreadMutex.instance("ActiveClient").addOnThreadReleasedEvent(new OnReleaseThread() {
            @Override
            public void threadReleased() {
                final Client client = Client.getActiveClient();
                if(preLoad)
                    loadAccountsLocally(client);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                }).start();
            }
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
            mSscTokenRequester.getTokenAsync(new SscTokenReceivedCallback() {
                @Override
                public void onSscTokenReceived(WSResponse<SscToken> wsTokenResult) {
                    new AccountWS(wsTokenResult.getResult()).getAllAccounts(client.getGmail(),
                            Build.BRAND, Build.MODEL, view.getImei(), gcmToken,
                            new IWSFinishEvent<List<Account>>() {
                                @Override
                                public void executeOnFinished(final WSResponse<List<Account>> result) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (result.getErrors().size() == 0) {
                                                final List<Account> accounts =
                                                        ClientManager.registerClientAccounts(result.getResult());
                                                view.hideWaiting();
                                                view.showAccounts(accounts);
                                            } else {
                                                view.hideWaiting();
                                                if (mIsRefreshing)
                                                    view.showViewAccountsErrors(result.getErrors());
                                            }
                                            mIsLoadingAccounts = false;
                                            mIsRefreshing = false;
                                        }
                                    }).start();
                                }
                            });
                }
            });
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
