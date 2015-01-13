package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.ActiveClientThreadMutex;
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
				view.getPreferences().setAppAlreadyUsed().setHasOneGmailAccount();
				ClientManager.RegisterClient(gmail);
				ActiveClientThreadMutex.setFree();
			}
		});
		ActiveClientThreadMutex.setBusy();
		thread.start();
		view.goToMainMenu();
	}
}
