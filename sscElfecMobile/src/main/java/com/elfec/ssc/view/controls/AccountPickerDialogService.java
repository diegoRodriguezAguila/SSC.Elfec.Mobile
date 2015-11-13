package com.elfec.ssc.view.controls;

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
import android.support.v7.app.AlertDialog;

import com.elfec.ssc.R;
import com.elfec.ssc.view.controls.events.OnAccountPicked;

import java.io.IOException;
import java.util.ArrayList;

public class AccountPickerDialogService {

    private final String GOOGLE_ACCOUNT_TYPE = "com.google";
    private AlertDialog.Builder mDialogBuilder;
    private OnAccountPicked onAccountPicked;
    private int mSelectedItem;
    private ArrayList<String> mGoogleAccounts;

    public AccountPickerDialogService(Activity activity, OnAccountPicked onAccountPicked) {
        this(activity, onAccountPicked, null);
    }

    public AccountPickerDialogService(final Activity activity, OnAccountPicked onAccountPicked, String activeClientGmail) {
        this.onAccountPicked = onAccountPicked;
        if (activeClientGmail == null)
            initializePickList(activity);
        else initializePickListAndPreselect(activity, activeClientGmail);
        mDialogBuilder = new AlertDialog.Builder(activity);
        mDialogBuilder.setTitle(R.string.account_picker_title)
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
                .setSingleChoiceItems(mGoogleAccounts.toArray(new String[mGoogleAccounts.size()]),
                        mSelectedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelectedItem = which;
                            }
                        });
    }

    /**
     * Inicializa la lista de cuentas de google, y setea el item seleccionado por defecto
     *
     * @param activity activity
     */
    private void initializePickListAndPreselect(final Activity activity, String activeClientGmail) {
        int defaultItem = 0;
        mSelectedItem = defaultItem;
        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccounts();
        mGoogleAccounts = new ArrayList<>();
        for (Account ac : accounts) {
            if (ac.type.equals(GOOGLE_ACCOUNT_TYPE)) {
                mGoogleAccounts.add(ac.name);
                if (ac.name.equals(activeClientGmail))
                    mSelectedItem = defaultItem;
                defaultItem++;
            }
        }
        mGoogleAccounts.add("Agregar cuenta");
    }

    /**
     * Inicializa la lista de cuentas de google
     *
     * @param activity activity
     */
    private void initializePickList(final Activity activity) {
        mSelectedItem = 0;
        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccounts();
        mGoogleAccounts = new ArrayList<>();
        for (Account ac : accounts) {
            if (ac.type.equals(GOOGLE_ACCOUNT_TYPE)) {
                mGoogleAccounts.add(ac.name);
            }
        }
        mGoogleAccounts.add("Agregar cuenta");
    }

    private void handleAccountPick(final Activity activity) {
        if (mSelectedItem == mGoogleAccounts.size() - 1) {
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
                    } catch (AuthenticatorException | IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            AccountManager acm = AccountManager.get(activity);
            acm.addAccount(GOOGLE_ACCOUNT_TYPE, null, null, null,
                    activity, callback, null);
        } else // si selecionó una cuenta existente
        {
            AccountPickerDialogService.this.onAccountPicked
                    .onAccountPicked(mGoogleAccounts
                            .get(mSelectedItem));
        }
    }

    public void show() {
        mDialogBuilder.show();
    }
}
