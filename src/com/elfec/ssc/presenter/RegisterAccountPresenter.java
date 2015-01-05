package com.elfec.ssc.presenter;

import java.util.List;

import android.os.Build;

import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.presenter.views.IRegisterAccount;

public class RegisterAccountPresenter {

	private IRegisterAccount view;
	public RegisterAccountPresenter(IRegisterAccount view) {
		this.view = view;
	}
	
	public void submitAccountData()
	{
		AccountWS accountWebService = new AccountWS();
		accountWebService.registerAccount(view.getAccountNumber(), view.getNUS(),"pedro@gmail.com", view.getPhoneNumber(), 
				Build.BRAND , Build.MODEL, view.getIMEI(), "2131f1dsa13ffsddgh31vvasd", new IWSFinishEvent<List<Integer>>() {		
					@Override
					public void executeOnFinished(List<Integer> result) {
					}
				});
	}
}
