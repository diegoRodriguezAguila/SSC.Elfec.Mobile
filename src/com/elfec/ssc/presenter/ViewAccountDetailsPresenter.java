package com.elfec.ssc.presenter;

import java.util.List;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

public class ViewAccountDetailsPresenter {

	private IViewAccountDetails view;
	private Account accountToShow;
	
	public ViewAccountDetailsPresenter(IViewAccountDetails view, long accountToShowId) {
		this.view = view;
		this.accountToShow = Account.get(accountToShowId);
	}

	public void getUsage()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				view.showUsage(accountToShow.getUsages());
				new AccountWS()
				.getUsage(accountToShow.getNUS(), new IWSFinishEvent<List<Usage>>() {
			
					@Override
					public void executeOnFinished(final WSResponse<List<Usage>> result) {									
						if(result.getErrors().size()==0)
						{
							accountToShow.removeUsages();
							ElfecAccountsManager.registerAccountUsages(accountToShow, result.getResult());
							view.showUsage(result.getResult());
						}
					}});
			}
		}).start();
	}
	
	/**
	 * Asigna los datos a la vista
	 */
	public void setFields()
	{
		view.setAccountNumber(accountToShow.getAccountNumber());
		view.setNUS(accountToShow.getNUS());
		view.setOwnerClient(accountToShow.getAccountOwner());
		view.setEnergySupplyStatus(accountToShow.getEnergySupplyStatus());
		new Thread(new Runnable() {
			@Override
			public void run() {
				view.showDebts(accountToShow.getDebts());
				view.setTotalDebt(accountToShow.getTotalDebt());
			}
		}).start();
	}
	
}
