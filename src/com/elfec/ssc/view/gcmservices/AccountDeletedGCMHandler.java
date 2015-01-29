package com.elfec.ssc.view.gcmservices;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.view.ViewAccounts;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AccountDeletedGCMHandler implements IGCMHandler {

	private final int NOTIF_ID = 2;
	@Override
	public Class<? extends Activity> getActivityClass() {
		return ViewAccounts.class;
	}

	@Override
	public void handleGCMessage(Bundle messageInfo,	NotificationManager notifManager, NotificationCompat.Builder builder) {
		Client ownerClient = Client.getClientByGmail(messageInfo.getString("gmail"));
		if(ownerClient != null)
		{
			boolean res = ElfecAccountsManager.deleteAccount(ownerClient.getGmail(), messageInfo.getString("nus"));
			if(res)
			{
				ViewAccountsPresenter presenter = ViewPresenterManager
						.getPresenter();
				if (presenter != null)
					presenter.gatherAccounts();
				notifManager.notify(NOTIF_ID, builder.build());
			}
		}
	}

}
