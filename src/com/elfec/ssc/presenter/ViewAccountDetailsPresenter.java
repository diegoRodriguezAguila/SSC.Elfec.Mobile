package com.elfec.ssc.presenter;

import java.util.List;

import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

public class ViewAccountDetailsPresenter {

	private IViewAccountDetails view;
	private Account accountToShow;

	public void getUsage()
	{
		AccountWS ws=new AccountWS();
		ws.getUsage(accountToShow.getNUS(), new IWSFinishEvent<List<Usage>>() {
			
			@Override
			public void executeOnFinished(final WSResponse<List<Usage>> result) {
				Thread thread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(result.getErrors().size()==0)
						{
							view.showUsage(result.getResult());
						}
					}
				});
				thread.start();
			}
		});
	}
	
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
	}
	
}
