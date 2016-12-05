package com.elfec.ssc.presenter;

import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.UsageManager;
import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.local_storage.UsageStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.views.IAccountDetailsView;

import java.net.ConnectException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountDetailsPresenter extends BasePresenter<IAccountDetailsView> {
    private Account account;

    public AccountDetailsPresenter(IAccountDetailsView view) {
        super(view);
    }

    public void loadAccount(String nus) {
        ClientManager.activeClient()
                .flatMap(client -> new AccountStorage().getAccount(client.getGmail(), nus))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(account -> {
                    this.account = account;
                    mView.onLoaded(account);
                }, mView::onError);
    }

    public void loadUsages(String nus) {
        new UsageStorage().getUsages(nus)
                .flatMap(usages -> {
                    mView.onUsageLoaded(usages);
                    return UsageManager.syncUsages(nus);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::onUsageLoaded, e -> {
                    if (!(e instanceof ConnectException)) {
                        mView.onError(e);
                    }
                });
    }

    /**
     * LLama a los métodos necesarios para ir a la dirección
     */
    public void goToAddress() {
        mView.navigateToAddress(account);
    }

}
