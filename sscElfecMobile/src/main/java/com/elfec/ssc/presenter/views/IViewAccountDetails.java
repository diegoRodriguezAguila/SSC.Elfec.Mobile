package com.elfec.ssc.presenter.views;

import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Provee de una abstracci�n de la vista de información de una cuenta
 */
public interface IViewAccountDetails {
    void setAccountNumber(String accountNumber);

    void setNUS(String nus);

    void setOwnerClient(String ownerClient);

    void setClientAddress(String address);

    void setEnergySupplyStatus(
            AccountEnergySupplyStatus energySupplyStatus);

    void setTotalDebt(BigDecimal totalDebt);

    void showUsage(List<Usage> usage);

    void showDebts(List<Debt> debts);

    void navigateToAddress(Account account);

    /**
     * Obtiene el WSTokenRequester con el contexto de la actividad actual
     *
     * @return
     */
    WSTokenRequester getWSTokenRequester();
}
