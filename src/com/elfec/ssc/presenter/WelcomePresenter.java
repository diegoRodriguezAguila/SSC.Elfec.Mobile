package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.presenter.views.IWelcome;

public class WelcomePresenter {

	private IWelcome view;
	
	public WelcomePresenter(IWelcome view)
	{
		this.view = view;
	}
	
	public void handlePickedGmailAccount(final String gmail)
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				ClientManager.RegisterClient(gmail);
				view.getPreferences().setAppAlreadyUsed().setHasOneGmailAccount();
			}
		});
		thread.start();
		view.goToMainMenu();
	}
}
