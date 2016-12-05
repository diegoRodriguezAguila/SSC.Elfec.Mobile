package com.elfec.ssc.presenter.views;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Usage;

import java.util.List;

/**
 * Provee de una abstracción de la vista de información de una cuenta
 */
public interface IAccountDetailsView extends ILoadView<Account> {
    void onUsageLoaded(List<Usage> usage);
    void navigateToAddress(Account account);

}
