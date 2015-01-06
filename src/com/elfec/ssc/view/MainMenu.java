package com.elfec.ssc.view;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.presenter.MainMenuPresenter;
import com.elfec.ssc.presenter.views.IMainMenu;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenu extends ActionBarActivity implements IMainMenu {

	@SuppressWarnings("unused")
	private MainMenuPresenter presenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		presenter = new MainMenuPresenter(this);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void btnAccountsClick(View view)
	{
		Intent i = new Intent(MainMenu.this, ViewAccounts.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out); 
	}
	
	public void btnLocationServicesClick(View view)
	{
		
	}
	
	public void btnNotificationsClick(View view)
	{
		
	}
	
	public void btnContactsClick(View view)
	{
		
	}
	
	public void showDialog(View v)
    {
    	AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        ArrayList<String> googleAccounts = new ArrayList<String>();
        for (Account ac : accounts) {
            if(ac.type.equals("com.google")) {
                googleAccounts.add(ac.name);
            }
        }
        googleAccounts.add("Agregar cuenta");
         (new AlertDialogPro.Builder(this)).setTitle("ELIGE UNA CUENTA")
         .setPositiveButton("Aceptar", new OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {		
 				AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>(){

					@Override
					public void run(AccountManagerFuture<Bundle> future) {
						Bundle res;
						try {
							res = future.getResult();
							String gmailNuevo = res.getString(AccountManager.KEY_ACCOUNT_NAME);
						} catch (OperationCanceledException e) {
							e.printStackTrace();
						} catch (AuthenticatorException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
 					
 				};
 				 AccountManager acm = AccountManager.get(getApplicationContext());
  				 acm.addAccount("com.google", null, null, null, MainMenu.this, 
  	             callback, null);

 			}
 		}).setNegativeButton("Cancelar", null)
         .setSingleChoiceItems(googleAccounts.toArray(new String[googleAccounts.size()]),
                        0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
                            }
                        }).show();
    }
}
