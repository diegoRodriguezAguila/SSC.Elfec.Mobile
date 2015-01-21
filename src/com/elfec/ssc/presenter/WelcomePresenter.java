package com.elfec.ssc.presenter;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.model.Contact;
import com.elfec.ssc.presenter.views.IWelcome;

public class WelcomePresenter {

	private IWelcome view;
	private final String PHONE="176";
	private final String ADDRESS="Av. Heroinas entre C. Suipacha y C. Costanera #686";
	private final String EMAIL="elfec@elfec.com";
	private final String WEB_PAGE="www.elfec.com.bo";
	private final String FACEBOOK="m.facebook.com/elfec";
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
	public void insertContact()
	{
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(Contact.getAll(Contact.class).size()==0)
				{
					ThreadMutex.instance("InsertContact").setBusy();
					Contact defaultContact=new Contact(PHONE, ADDRESS, EMAIL, WEB_PAGE, FACEBOOK);
					defaultContact.save();
					ThreadMutex.instance("InsertContact").setFree();
				}
			}
		});
		thread.start();
	}
}
