package com.elfec.ssc.presenter;

import java.util.List;

import android.os.Looper;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.WSTokenReceivedCallback;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

public class ViewAccountDetailsPresenter {

	private IViewAccountDetails view;
	private Account accountToShow;

	public ViewAccountDetailsPresenter(IViewAccountDetails view,
			long accountToShowId) {
		this.view = view;
		this.accountToShow = Account.get(accountToShowId);
	}

	public void getUsage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				view.showUsage(accountToShow.getUsages());
				view.getWSTokenRequester().getTokenAsync(
						new WSTokenReceivedCallback() {
							@Override
							public void onWSTokenReceived(
									WSResponse<WSToken> wsTokenResult) {
								if (wsTokenResult.getResult() != null)
									new AccountWS(wsTokenResult.getResult()).getUsage(
											accountToShow.getNUS(),
											new IWSFinishEvent<List<Usage>>() {

												@Override
												public void executeOnFinished(
														final WSResponse<List<Usage>> result) {
													if (result.getErrors()
															.size() == 0) {
														accountToShow
																.removeUsages();
														ElfecAccountsManager
																.registerAccountUsages(
																		accountToShow,
																		result.getResult());
														view.showUsage(result
																.getResult());
													}
												}
											});
							}
						});
				Looper.loop();
			}
		}).start();
	}

	/**
	 * Asigna los datos a la vista
	 */
	public void setFields() {
		view.setAccountNumber(accountToShow.getAccountNumber());
		view.setNUS(accountToShow.getNUS());
		view.setOwnerClient(accountToShow.getAccountOwner());
		view.setClientAddress(accountToShow.getAddress());
		view.setEnergySupplyStatus(accountToShow.getEnergySupplyStatus());
		new Thread(new Runnable() {
			@Override
			public void run() {
				view.showDebts(accountToShow.getDebts());
				view.setTotalDebt(accountToShow.getTotalDebt());
			}
		}).start();
	}

	/**
	 * LLama a los métodos necesarios para ir a la direcció
	 */
	public void goToAddress() {
		view.navigateToAddress(accountToShow);
	}

}
