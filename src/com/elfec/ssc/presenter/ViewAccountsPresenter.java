package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.presenter.views.IViewAccounts;

public class ViewAccountsPresenter {

	private IViewAccounts view;
	
	public ViewAccountsPresenter(IViewAccounts view)
	{
		this.view = view;
	}
	
	public void invokeAccountWS()
	{
		view.equals(null);
		AccountWS accountWS = new AccountWS();
		accountWS.registerAccount("12345", "654321", "jarry@gmail.com", "72993222", "Samsung", 
				"Galaxy S3", "333255112223", "1a2b3c4d5e6e7f8g9h10i");
	}
}
