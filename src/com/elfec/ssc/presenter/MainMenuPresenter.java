package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.model.Client;
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
		ThreadMutex.instance("ActiveClient").setBusy();
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				ClientManager.registerClient(gmail);
				view.getPreferences().setHasOneGmailAccount();
				ThreadMutex.instance("ActiveClient").setFree();
			}
		});
		thread.start();
		view.goToViewAccounts();
	}
	
	/**
	 * obtiene el cliente actual
	 */
	public void loadCurrentClient()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				Client client = Client.getActiveClient();
				view.setCurrentClient(client==null?null:client.getGmail());
			}
		});
		thread.start();
	}
}
