package com.elfec.ssc.presenter.views;

import java.util.List;

import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;

public interface IViewAccounts {
	//temporal
	public void show(List<Account> result);
	/**
	 * Obtiene el PreferencesManager con el contexto de la aplicación global
	 * @return {@link PreferencesManager}
	 */
	public PreferencesManager getPreferences();
	public String getIMEI();
	public void refreshAccounts();
	public void displayErrors(List<Exception> errors);
	public void dialogRemove(int position); 
	public void ShowWaitingWS();
	public void hideWSWaiting();
	public boolean isRefreshed();
	public void showViewAccountsErrors(List<Exception> errors);
	/**
	 * Obtiene el GCMTokenRequester con el contexto de la actividad actual
	 * @return
	 */
	public GCMTokenRequester getGCMTokenRequester();
}
