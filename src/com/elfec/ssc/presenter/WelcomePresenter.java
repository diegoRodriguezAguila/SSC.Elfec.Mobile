package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.model.Contact;
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
				ClientManager.registerActiveClient(gmail);
				ThreadMutex.instance("ActiveClient").setFree();
			}
		});
		ThreadMutex.instance("ActiveClient").setBusy();
		thread.start();
		view.goToMainMenu();
	}
	public void insertContact()
	{
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(Contact.getAll(Contact.class).size()==0)
				{
					ThreadMutex.instance("InsertContact").setBusy();
					Contact.createDefaultContact();
					ThreadMutex.instance("InsertContact").setFree();
				}
			}
		});
		thread.start();
	}
}
