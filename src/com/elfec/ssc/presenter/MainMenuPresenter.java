package com.elfec.ssc.presenter;

import java.io.IOException;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.presenter.views.IMainMenu;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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
		ThreadMutex.instance("ActiveClient").setBusy();
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {

				ClientManager.RegisterClient(gmail);
				
				view.getPreferences().setHasOneGmailAccount();
				ThreadMutex.instance("ActiveClient").setFree();
			}
		});
		thread.start();
		view.goToViewAccounts();
	}
}
