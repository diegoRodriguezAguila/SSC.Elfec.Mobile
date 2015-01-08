package com.elfec.ssc.presenter;

import android.os.Build;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IRegisterAccount;

/**
 * Presenter para la vista {@link IRegisterAccount}
 * @author Diego
 *
 */
public class RegisterAccountPresenter {

	private IRegisterAccount view;
	public RegisterAccountPresenter(IRegisterAccount view) {
		this.view = view;
	}

	/**
	 * Obtiene la información provista por el usuario y del dispositivo
	 */
	public void processAccountData()
	{
		final Client client = Client.getActiveClient();
		final Thread thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				ElfecAccountsManager.RegisterAccount(client, view.getAccountNumber(), view.getNUS());
			}
		});
		AccountWS accountWebService = new AccountWS();
		accountWebService.registerAccount(view.getAccountNumber(), view.getNUS(), client.getGmail(), view.getPhoneNumber(), 
				Build.BRAND , Build.MODEL, view.getIMEI(), "2131f1dsa13ffsddgh31vvasd", new IWSFinishEvent<Boolean>() {		
					@Override
					public void executeOnFinished(WSResponse<Boolean> result) 
					{
						thread.start();
					}
				});
	}
}
