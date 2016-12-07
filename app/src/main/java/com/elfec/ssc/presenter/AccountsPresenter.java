package com.elfec.ssc.presenter;

import com.elfec.ssc.business_logic.AccountManager;
import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.DeviceManager;
import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.exceptions.OutdatedAppException;
import com.elfec.ssc.presenter.views.IAccountsView;
import com.elfec.ssc.security.AppPreferences;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountsPresenter extends BasePresenter<IAccountsView> {
    private String mGmail;

    public AccountsPresenter(IAccountsView view) {
        super(view);
        checkGcmToken();
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
                .subscribe(acc -> {
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

    /**
     * Checks if the token should be sent and send it if possible
     */
    private void checkGcmToken() {
        if (!AppPreferences.instance().hasToSendGcmToken()) return;
        new DeviceManager().syncGcmToken()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(success -> {
                }, error -> {
                });
    }
}
