package com.elfec.ssc.presenter;

import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.WSTokenReceivedCallback;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.security.AppPreferences;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class ViewAccountsPresenter {

    private IViewAccounts view;
    private boolean mIsLoadingAccounts;
    private boolean mIsRefreshing;

    public ViewAccountsPresenter(IViewAccounts view) {
        this.view = view;
        mIsLoadingAccounts = false;
        mIsRefreshing = false;
    }

    public void invokeRemoveAccountWS(final String nus) {
        final String imei = view.getImei();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                view.getWSTokenRequester().getTokenAsync(new WSTokenReceivedCallback() {
                    @Override
                    public void onWSTokenReceived(WSResponse<WSToken> wsTokenResult) {
                        final Client client = Client.getActiveClient();
                        new AccountWS(wsTokenResult.getResult()).removeAccount(client.getGmail(), nus, imei, new IWSFinishEvent<Boolean>() {
                            @Override
                            public void executeOnFinished(WSResponse<Boolean> result) {
                                if (result.getResult()) {
                                    ElfecAccountsManager.deleteAccount(client.getGmail(), nus);
                                    gatherAccounts();
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
        });
        thread.start();
    }

    /**
     * Obtiene las cuentas del cliente tanto remota como localmente
     */
    public void gatherAccounts() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final Client client = Client.getActiveClient();
                    GCMTokenRequester gcmTokenRequester = view.getGCMTokenRequester();
                    gcmTokenRequester.getTokenAsync(new GcmTokenCallback() {
                        @Override
                        public void onGcmTokenReceived(String gcmToken) {
                            if (gcmToken == null) {
                                view.hideWaiting();
                                List<Exception> errorsToShow = new ArrayList<>();
                                errorsToShow.add(new ConnectException("No fue posible conectarse con el servidor, porfavor revise su conexión a internet.\n\u25CF También verifique que tiene Google Play Services instalado."));
                                view.showViewAccountsErrors(errorsToShow);
                                view.showAccounts(null);
                            } else {
                                loadAccountsRemotely(client);
                            }
                        }
                    });
                Looper.loop();
            }
        });
        ThreadMutex.instance("ActiveClient").addOnThreadReleasedEvent(new OnReleaseThread() {
            @Override
            public void threadReleased() {
                thread.start();
            }
        });
    }

    /**
     * Actualiza las cuentas a partir de los web services
     */
    public void refreshAccountsRemotely() {
        mIsRefreshing = true;
        loadAccountsRemotely(Client.getActiveClient());
    }

    /**
     * Invoca a las clases necesarias para obtener las cuentas del cliente via web services
     *
     * @param client cliente
     */
    private void loadAccountsRemotely(final Client client) {
        if (!mIsLoadingAccounts) {
            mIsLoadingAccounts = true;
            view.getWSTokenRequester().getTokenAsync(new WSTokenReceivedCallback() {
                @Override
                public void onWSTokenReceived(WSResponse<WSToken> wsTokenResult) {
                    new AccountWS(wsTokenResult.getResult()).getAllAccounts(client.getGmail(),
                        Build.BRAND, Build.MODEL, view.getImei(), AppPreferences.instance().getGCMToken(),
                        new IWSFinishEvent<List<Account>>() {
                            @Override
                            public void executeOnFinished(final WSResponse<List<Account>> result) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (result.getErrors().size() == 0) {
                                            final List<Account> accounts = ClientManager.registerClientAccounts(result.getResult());
                                            view.hideWaiting();
                                            view.showAccounts(accounts);
                                        } else {
                                            view.hideWaiting();
                                            view.showViewAccountsErrors(result.getErrors());
                                            if (!mIsRefreshing)
                                                view.showAccounts(null);
                                            else loadAccountsLocally(client);
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
    private void loadAccountsLocally(Client client) {
        List<Account> activeAccounts = client.getActiveAccounts();
        view.hideWaiting();
        view.showAccounts(activeAccounts);
    }

}
