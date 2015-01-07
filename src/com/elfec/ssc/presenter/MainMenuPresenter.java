package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.presenter.views.IMainMenu;

public class MainMenuPresenter {

	private IMainMenu view;
	public MainMenuPresenter(IMainMenu view)
	{
		this.view = view;
	}
	
	public void verifyAccountsRequirements()
	{
		if(view.getPreferences().hasOneGmailAccount())
		{
			view.goToViewAccounts();
		}
		else
		{
			view.warnUserHasNoAccounts();
		}
	}
	
	public void handlePickedGmailAccount(final String gmail)
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				ClientManager.RegisterClient(gmail);
				view.getPreferences().setHasOneGmailAccount();
			}
		});
		thread.start();
		view.goToViewAccounts();
	}
}
