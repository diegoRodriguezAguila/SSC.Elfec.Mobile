package com.elfec.ssc.presenter;

import android.content.Context;

import com.elfec.ssc.business_logic.AccountManager;
import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.exceptions.OutdatedAppException;
import com.elfec.ssc.presenter.views.IAccountsView;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;
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
     * @param account to remove
     */
    public void removeAccount(Account account) {
        cancelSubscription();
        mSubscription = ClientManager.activeClient()
                .flatMap(client -> {
                    mGmail = client.getGmail();
                    return AccountManager.removeAccount(mGmail, account);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(acc->{
                    loadAccounts();
                    mView.onSuccess(acc);
                }, mView::onError);
    }

    /**
     * Obtiene las cuentas del cliente remotamente, si no hay conexión obtiene la
     * versión local
     */
    public void loadAccounts() {
        cancelSubscription();
        mSubscription = ClientManager.activeClient()
                .flatMap(client -> {
                    mGmail = client.getGmail();
                    return new AccountStorage().getAccounts(mGmail);
                })
                .flatMap(accounts -> {
                    mView.onLoaded(accounts);
                    return AccountManager.syncAccounts(mGmail);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::onLoaded, e -> {
                    if (e instanceof OutdatedAppException) {
                        mView.onError(e);
                    }
                });
    }

    /**
     * Obtiene las cuentas del cliente remotamente
     */
    public void refreshAccounts() {
        cancelSubscription();
        mSubscription = AccountManager.syncAccounts(mGmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::onLoaded, mView::onError);
    }
}
