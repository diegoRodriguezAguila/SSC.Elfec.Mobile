package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.view.adapters.ViewAccountsAdapter;

public class ViewAccounts extends ActionBarActivity implements IViewAccounts {

	private ViewAccountsPresenter presenter;
	private ListView accounts;
	@Override
	protected void onResume()
	{
		super.onResume();
	      presenter.invokeAccountWS();
	      
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);
        presenter = new ViewAccountsPresenter(this);
        accounts=(ListView)findViewById(R.id.accounts_list);
        
        accounts.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				dialogRemove(position);
				return false;
			}
        }); 
      //  accounts.setFastScrollEnabled(true);
        presenter.invokeAccountWS();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    
    @Override
	public String getIMEI() {
		return ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_accounts, menu);
        return true;
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
	public void show(final List<com.elfec.ssc.model.Account> result) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ViewAccountsAdapter adapter=new ViewAccountsAdapter(ViewAccounts.this, R.layout.simple_row, result);
				accounts.setAdapter(adapter);

			}
		});
	}

	@Override
	public PreferencesManager getPreferences() {
		return new PreferencesManager(getApplicationContext());
	}

	@Override
	public void refreshAccounts() {
	      presenter.invokeAccountWS();

		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {

				Toast.makeText(getApplicationContext(), "En teoria esta borrado",
						   Toast.LENGTH_LONG).show();
	
			}
		});
	}

	@Override
	public void displayErrors(List<Exception> errors) {
		 String result="";
		for(Exception error : errors)
		{
			result+=error.getMessage()+"\n";
		}
		final String exceptions=result;
			runOnUiThread(new Runnable() {
			
			@Override
			public void run() {

				Toast.makeText(getApplicationContext(), exceptions,
						   Toast.LENGTH_LONG).show();
	
			}
		});
		
	}
	
	public void dialogRemove(final int position)
    {
    	(new AlertDialogPro.Builder(this)).setTitle("Eliminar")
        .setMessage("Esta seguro que desea eliminar esta cuenta?")
        .setPositiveButton("Si", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Account account=(Account)accounts.getAdapter().getItem(position);
				presenter.invokeRemoveAccountWS(account.getNUS());
				
			}
		}).setNegativeButton("No", null).show();
    }

}
