package com.elfec.ssc.presenter;

import java.util.List;

import org.joda.time.DateTime;

import android.os.Looper;

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
				Looper.prepare();
				AccountWS accountWS = new AccountWS();
				accountWS.getAllAccounts(Client.getActiveClient().getGmail(), new IWSFinishEvent<List<Account>>() 
						{
							@Override
							public void executeOnFinished(WSResponse<List<Account>> result) {
								List<Account> accounts = result.getResult();
								for (Account account : accounts) {
									account.setInsertDate(DateTime.now());
									account.save();
								}
								view.show(result.getResult());
							}

						});
				Looper.loop();
			}
		});
		thread.start();
	}
	
}
