package com.elfec.ssc.presenter;

import android.os.Looper;

import com.elfec.ssc.business_logic.ElfecAccountsManager;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.views.IViewAccountDetails;
import com.elfec.ssc.web_services.AccountWS;
import com.elfec.ssc.web_services.SscTokenRequester;

public class ViewAccountDetailsPresenter {

    private IViewAccountDetails view;
    private Account accountToShow;
    private SscTokenRequester mSscTokenRequester;

    public ViewAccountDetailsPresenter(IViewAccountDetails view,
                                       long accountToShowId) {
        this.view = view;
        this.accountToShow = Account.get(accountToShowId);
        mSscTokenRequester = new SscTokenRequester();
    }

    public void getUsage() {
        new Thread(() -> {
            Looper.prepare();
            view.showUsage(accountToShow.getUsages());
            mSscTokenRequester.getTokenAsync(wsTokenResult -> {
                if (wsTokenResult.getResult() == null) return;
                new AccountWS(wsTokenResult.getResult())
                        .getUsage(accountToShow.getNus(), result -> {
                            if (result.getErrors().size() > 0) return;
                            accountToShow.removeUsages();
                            ElfecAccountsManager
                                    .registerAccountUsages(accountToShow,result.getResult());
                            view.showUsage(result.getResult());
                        });
            });
            Looper.loop();
        }).start();
    }

    /**
     * Asigna los datos a la vista
     */
    public void setFields() {
        view.setAccountNumber(accountToShow.getAccountNumber());
        view.setNus(accountToShow.getNus());
        view.setOwnerClient(accountToShow.getAccountOwner());
        view.setClientAddress(accountToShow.getAddress());
        view.setEnergySupplyStatus(accountToShow.getEnergySupplyStatus());
        new Thread(() -> {
            view.showDebts(accountToShow.getDebts());
            view.setTotalDebt(accountToShow.getTotalDebt());
        }).start();
    }

    /**
     * LLama a los m�todos necesarios para ir a la direcci�
     */
    public void goToAddress() {
        view.navigateToAddress(accountToShow);
    }

}
