package com.elfec.ssc.presenter;

import com.elfec.ssc.business_logic.UsageManager;
import com.elfec.ssc.local_storage.UsageStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.views.IViewAccountDetails;
import com.elfec.ssc.web_services.SscTokenRequester;

import java.net.ConnectException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewAccountDetailsPresenter extends BasePresenter<IViewAccountDetails> {
    private Account account;

    public ViewAccountDetailsPresenter(IViewAccountDetails view,
                                       long accountId) {
        super(view);
        this.account = Account.get(accountId);
    }

    public void getUsages() {
        new UsageStorage().getUsages(account.getNus())
                .flatMap(usages -> {
                    mView.showUsage(usages);
                    return new SscTokenRequester().getSscToken();
                }).flatMap(sscToken ->
                UsageManager.syncUsages(account.getNus()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::showUsage, e -> {
                    if (!(e instanceof ConnectException)) {
                        mView.onError(e);
                    }
                });
    }

    /**
     * Asigna los datos a la vista
     */
    public void setFields() {
        mView.setAccountNumber(account.getAccountNumber());
        mView.setNus(account.getNus());
        mView.setOwnerClient(account.getAccountOwner());
        mView.setClientAddress(account.getAddress());
        mView.setEnergySupplyStatus(account.getEnergySupplyStatus());
        new Thread(() -> {
            mView.showDebts(account.getDebts());
            mView.setTotalDebt(account.getTotalDebt());
        }).start();
    }

    /**
     * LLama a los métodos necesarios para ir a la dirección
     */
    public void goToAddress() {
        mView.navigateToAddress(account);
    }

}
