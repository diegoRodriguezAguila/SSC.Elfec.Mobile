package com.elfec.ssc.presenter;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.businesslogic.ClientManager;
import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.helpers.ThreadMutex;
import com.elfec.ssc.helpers.threading.OnReleaseThread;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.GCMTokenReceivedCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccounts;

public class ViewAccountsPresenter {

	private IViewAccounts view;
	private boolean isLoadingAccounts;
	private boolean isRefreshing;
	
	public ViewAccountsPresenter(IViewAccounts view)
	{
		this.view = view;
		isLoadingAccounts=false;
		isRefreshing=false;
	}
	public void invokeRemoveAccountWS(final String nus)
	{
		final String imei=view.getIMEI();
		Thread thread=new Thread(new Runnable() {			
			@Override
			public void run() 
			{
				Looper.prepare();
				AccountWS accountWS = new AccountWS();
				final Client client=Client.getActiveClient();
				accountWS.removeAccount(client.getGmail(), nus, imei, new IWSFinishEvent<Boolean>() {					
					@Override
					public void executeOnFinished(WSResponse<Boolean> result) {
						if(result.getResult())
						{
							ElfecAccountsManager.deleteAccount(client.getGmail(), nus);
							view.refreshAccounts();
							view.hideWSWaiting();
						}
						else
						{
							view.hideWSWaiting();
							view.displayErrors(result.getErrors());
						}
						
						
					}
				});				
				Looper.loop();
			}
		});
		thread.start();
	}
	
	/**
	 * Obtiene las cuentas del cliente tanto remota como localmente
	 */
	public void gatherAccounts()
	{
		final Thread thread=new Thread(new Runnable() {			
			@Override
			public void run() 
			{
				Looper.prepare();
				final Client client=Client.getActiveClient();
				if(view.getPreferences().isFirstLoadAccounts() )
				{
					GCMTokenRequester gcmTokenRequester = view.getGCMTokenRequester();
					gcmTokenRequester.getTokenAsync(new GCMTokenReceivedCallback() {								
						@Override
						public void onGCMTokenReceived(String deviceToken) {
							if(deviceToken==null)
							{
								view.hideWSWaiting();
								List<Exception> errorsToShow = new ArrayList<Exception>();
								errorsToShow.add(new ConnectException("No fue posible conectarse con el servidor, porfavor revise su conexión a internet.\n● También verifique que tiene Google Play Services instalado."));
								view.showViewAccountsErrors(errorsToShow);
								view.showAccounts(null);
							}
							else
							{
								callGetAllAccountsWebService(client);
							}
						}
					});
				}
				else
				{
					loadLocalAccounts(client);
				}
				Looper.loop();
			}
		});
		ThreadMutex.instance("ActiveClient").addOnThreadReleasedEvent(new OnReleaseThread() {
			@Override
			public void threadReleased() {
				thread.start();
			}
		});
	}
	public void getAllServiceAccounts()
	{
		Client client=Client.getActiveClient();
		isRefreshing=true;
		callGetAllAccountsWebService(client);
	}
	/**
	 * Invoca a las clases necesarias para obtener las cuentas del cliente via web services
	 * @param accountWS
	 * @param client
	 */
	private void callGetAllAccountsWebService(final Client client) {
		if(!isLoadingAccounts)
		{
			isLoadingAccounts=true;
			AccountWS accountWS = new AccountWS();
			accountWS.getAllAccounts(client.getGmail(), Build.BRAND , Build.MODEL, view.getIMEI(), view.getPreferences().getGCMToken(), 
				new IWSFinishEvent<List<Account>>() 
			{
				@Override
				public void executeOnFinished(final WSResponse<List<Account>> result) 
				{						
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							if(result.getErrors().size()==0)
							{
								final List<Account> accounts=ClientManager.registerClientAccounts(result.getResult());
								view.getPreferences().setLoadAccountsAlreadyUsed();
								view.hideWSWaiting();	
								view.showAccounts(accounts);
							}
							else
							{
								view.hideWSWaiting();	
								view.showViewAccountsErrors(result.getErrors());
								if(view.getPreferences().isFirstLoadAccounts() && !isRefreshing)
									view.showAccounts(null);
								else loadLocalAccounts(client);
							}
							isLoadingAccounts=false;
							isRefreshing=false;
						}
					}).start();
					}
			});
		}
	}
	
	/**
	 * Obtiene las cuentas locales del cliente
	 * @param client
	 */
	public void loadLocalAccounts(Client client)
	{
		List<Account> activeAccounts = client.getActiveAccounts();
		view.hideWSWaiting();	
		view.showAccounts(activeAccounts);
	}
	
}
