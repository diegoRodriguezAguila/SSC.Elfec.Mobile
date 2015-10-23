package com.elfec.ssc.view.controls;

import java.io.IOException;
import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.view.controls.events.OnAccountPicked;

public class AccountPickerDialogService {

	private final String GOOGLE_ACCOUNT_TYPE = "com.google";
	private AlertDialogPro.Builder dialogBuilder;
	private OnAccountPicked onAccountPicked;
	private int selectedItem;
	private  ArrayList<String> googleAccounts;
	
	public static AccountPickerDialogService instanceService(Activity activity, OnAccountPicked onAccountPicked)
	{
		return new AccountPickerDialogService(activity, onAccountPicked, null);
	}
	
	public static AccountPickerDialogService instanceService(Activity activity, OnAccountPicked onAccountPicked, String activeClientGmail)
	{
		return new AccountPickerDialogService(activity, onAccountPicked, activeClientGmail);
	}
	
	private AccountPickerDialogService(final Activity activity, OnAccountPicked onAccountPicked, String activeClientGmail)
	{
		this.onAccountPicked = onAccountPicked;
		if(activeClientGmail==null)
			initializePickList(activity);
		else initializePickListAndPreselect(activity, activeClientGmail);
		dialogBuilder = new AlertDialogPro.Builder(activity);
		dialogBuilder.setTitle(R.string.account_picker_title)
		.setIcon(R.drawable.gmail_icon)
        .setPositiveButton(R.string.btn_ok, new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				handleAccountPick(activity);
			}		
		})
		.setNegativeButton(R.string.btn_cancel, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AccountPickerDialogService.this.onAccountPicked.onPickedCanceled();
			}
		})
        .setSingleChoiceItems(googleAccounts.toArray(new String[googleAccounts.size()]),
        		selectedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        	    selectedItem = which;
            }
        });
	}

	/**
	 * Inicializa la lista de cuentas de google, y setea el item seleccionado por defecto
	 * @param activity
	 */
	private void initializePickListAndPreselect(final Activity activity, String activeClientGmail) {
        int defaultItem = 0;
		selectedItem = defaultItem;
		AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccounts();
        googleAccounts = new ArrayList<String>();
        for (Account ac : accounts) 
        {
            if(ac.type.equals(GOOGLE_ACCOUNT_TYPE)) 
            {
                googleAccounts.add(ac.name);
        		if(ac.name.equals(activeClientGmail))
    			{
					selectedItem = defaultItem;
    			}
        		defaultItem++;
    		}
        }
        googleAccounts.add("Agregar cuenta");
	}
	
	/**
	 * Inicializa la lista de cuentas de google
	 * @param activity
	 */
	private void initializePickList(final Activity activity) {;
		selectedItem = 0;
		AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccounts();
        googleAccounts = new ArrayList<String>();
        for (Account ac : accounts) 
        {
            if(ac.type.equals(GOOGLE_ACCOUNT_TYPE)) 
            {
                googleAccounts.add(ac.name);
    		}
        }
        googleAccounts.add("Agregar cuenta");
	}
	
	private void handleAccountPick(final Activity activity) {
		if (selectedItem == googleAccounts.size() - 1) {
			AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {
				@Override
				public void run(
						AccountManagerFuture<Bundle> future) {
					Bundle res;
					try {
						res = future.getResult();
						String gmailNuevo = res
								.getString(AccountManager.KEY_ACCOUNT_NAME);
						AccountPickerDialogService.this.onAccountPicked
								.onAccountPicked(gmailNuevo);
					} catch (OperationCanceledException e) {
						AccountPickerDialogService.this.onAccountPicked
								.onPickedCanceled();
					} catch (AuthenticatorException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			AccountManager acm = AccountManager.get(activity);
			acm.addAccount(GOOGLE_ACCOUNT_TYPE, null, null, null,
					activity, callback, null);
		} 
		else // si selecionó una cuenta existente
		{
			AccountPickerDialogService.this.onAccountPicked
					.onAccountPicked(googleAccounts
							.get(selectedItem));
		}
	}
	
	public void show()
	{
		dialogBuilder.show();
	}
}
