package com.elfec.ssc.presenter.views;

import com.elfec.ssc.model.Account;

import java.util.List;

public interface IAccountsView extends ILoadView<List<Account>> {

    /**
     * Muestra un mensaje de que la cuenta se borró exitosamente
     */
    void showAccountDeleted();


    void dialogRemove(int position);

    /**
     * Muestra un dialogo de espera
     */
    void showWaiting();

    /**
     * Oculta el dialogo de espera
     */
    void hideWaiting();
}
