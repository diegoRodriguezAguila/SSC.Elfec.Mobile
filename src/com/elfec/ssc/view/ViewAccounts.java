package com.elfec.ssc.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.view.adapters.ViewAccountsAdapter;

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
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
public class ViewAccounts extends ActionBarActivity implements IViewAccounts {

	private ViewAccountsPresenter presenter;
	private ListView accounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);
        presenter = new ViewAccountsPresenter(this);
        presenter.invokeAccountWS();
        accounts=(ListView)findViewById(R.id.accounts_list);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_accounts, menu);
        return true;
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
  				 acm.addAccount("com.google", null, null, null, ViewAccounts.this, 
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
    
    public void btnRegisterAccountClick(View view)
    {
    	Intent i = new Intent(ViewAccounts.this, RegisterAccount.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out); 
    }
    
    @Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}

	@Override
	public void show(List<com.elfec.ssc.model.Account> result) {
		ViewAccountsAdapter adapter=new ViewAccountsAdapter(this, R.layout.simple_row, result);
		accounts.setAdapter(adapter);
	}

	
}
