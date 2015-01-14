package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.ThreadMutex;
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
				ThreadMutex.instance("ActiveClient").setFree();
			}
		});
		ThreadMutex.instance("ActiveClient").setBusy();
		thread.start();
		view.goToMainMenu();
	}
}
