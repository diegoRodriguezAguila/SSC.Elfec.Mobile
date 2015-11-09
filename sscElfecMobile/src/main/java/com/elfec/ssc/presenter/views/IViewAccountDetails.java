package com.elfec.ssc.presenter.views;

import java.math.BigDecimal;
import java.util.List;

import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;

/**
 * Provee de una abstracción de la vista de información de una cuenta
 */
public interface IViewAccountDetails {
	public void setAccountNumber(String accountNumber);

	public void setNUS(String nus);

	public void setOwnerClient(String ownerClient);

	public void setClientAddress(String address);

	public void setEnergySupplyStatus(
			AccountEnergySupplyStatus energySuppluStatus);

	public void setTotalDebt(BigDecimal totalDebt);

	public void showUsage(List<Usage> usage);

	public void showDebts(List<Debt> debts);

	public void navigateToAddress(Account account);

	/**
	 * Obtiene el WSTokenRequester con el contexto de la actividad actual
	 * 
	 * @return
	 */
	public WSTokenRequester getWSTokenRequester();
}
