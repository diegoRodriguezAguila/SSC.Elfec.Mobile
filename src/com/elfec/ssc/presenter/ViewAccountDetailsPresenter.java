package com.elfec.ssc.presenter;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

public class ViewAccountDetailsPresenter {

	private IViewAccountDetails view;
	private Account accountToShow;

	public ViewAccountDetailsPresenter(IViewAccountDetails view, Account accountToShow) {
		this.view = view;
		this.accountToShow = accountToShow;
		setFields();
	}
	
	/**
	 * Asigna los datos a la vista
	 */
	private void setFields()
	{
		view.setAccountNumber(accountToShow.getAccountNumber());
		view.setNUS(accountToShow.getNUS());
		view.setOwnerClient(accountToShow.getAccountOwner());
		view.setEnergySupplyStatus(accountToShow.getEnergySupplyStatus());
		view.setTotalDebt(accountToShow.getTotalDebt());
		view.showDebts(accountToShow.getDebts());
	}
	
}
