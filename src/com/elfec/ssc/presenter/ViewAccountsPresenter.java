package com.elfec.ssc.presenter;

import java.util.List;

import org.joda.time.DateTime;

import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccounts;

public class ViewAccountsPresenter {

	private IViewAccounts view;
	
	public ViewAccountsPresenter(IViewAccounts view)
	{
		this.view = view;
	}
	
	public void invokeAccountWS()
	{
		Thread thread=new Thread(new Runnable() {			
			@Override
			public void run() 
			{
				AccountWS accountWS = new AccountWS();
				accountWS.getAllAccounts(Client.getActiveClient().getGmail(), new IWSFinishEvent<List<Account>>() 
						{
							@Override
							public void executeOnFinished(WSResponse<List<Account>> result) 
							{
								final List<Account> accounts=result.getResult();
										for(Account account : accounts)
										{
											account.setInsertDate(DateTime.now());
											//account.save();
											view.show(result.getResult());
										}
							}

						});
			}
		});
		thread.start();
		/*accountWS.registerAccount("12345", "654321", "jarry@gmail.com", "72993222", "Samsung", 
				"Galaxy S3", "333255112223", "1a2b3c4d5e6e7f8g9h10i", new IWSFinishEvent<List<Integer>>() {
					
					@Override
					public void executeOnFinished(List<Integer> result) {
						
						
					}
				});*/
		
	}
	
}
