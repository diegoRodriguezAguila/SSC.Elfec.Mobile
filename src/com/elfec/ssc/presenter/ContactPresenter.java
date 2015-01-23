package com.elfec.ssc.presenter;

import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.model.Contact;
import com.elfec.ssc.presenter.views.IContact;

public class ContactPresenter {

	private IContact view;
	public ContactPresenter(IContact view)
	{
		this.view=view;
	}
	public void setDefaultData()
	{
		ThreadMutex.instance("InsertContact").addOnThreadReleasedEvent(new OnReleaseThread() {
			
			@Override
			public void threadReleased() {
				Thread thread=new Thread(new Runnable() {				
					@Override
					public void run() {
						Contact defaultContact = Contact.getDefaultContact();
						view.setData(defaultContact.getPhone(), defaultContact.getAddress(), defaultContact.getEmail(), defaultContact.getWebPage(), defaultContact.getFacebook(),
								defaultContact.getFacebookId());
					}
				});
				thread.start();
			}
		});
	}
}
