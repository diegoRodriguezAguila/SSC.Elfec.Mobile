package com.elfec.ssc.view;


import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.RegisterAccountPresenter;
import com.elfec.ssc.presenter.views.IRegisterAccount;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class RegisterAccount extends ActionBarActivity implements IRegisterAccount {

	private RegisterAccountPresenter presenter;
	private EditText txtNUS;
	private EditText txtAccountNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);
		presenter = new RegisterAccountPresenter(this);
		txtNUS = (EditText) findViewById(R.id.txt_nus);		
		txtAccountNumber = (EditText) findViewById(R.id.txt_accountNumber);
		setOnFocusChangedListeners();
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
    @Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
    
    /**
     * Asigna los onFocusChange listeners del txtNus y el txtAccountNumber
     */
    public void setOnFocusChangedListeners()
    {
    	Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				txtNUS.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateNUS();
						}
					}
				});
				txtAccountNumber.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateAccountNumber();
						}
					}
				});
			}
		});
    	thread.start();
    }

    /**
     * Convierte una lista de errores en el formato html
     * necesario para mostrarlo en el metodo setError
     * @param validationErrors
     * @return
     */
	public String getHTMLListFromErrors(List<String> validationErrors)
	{
		StringBuilder str = new StringBuilder("<font color='#006086'><b>");
		int size = validationErrors.size();
		for (int i = 0; i < size; i++) {
			str.append("● ").append(validationErrors.get(i));
			str.append((i<size-1?"<br/>":""));
		}
		str.append("</b></font>");
		return str.toString();
	}

    
    //#region  Interface IRegisterAccount methods

	@Override
	public void setNUSErrors(final List<String> validationErrors) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				if(validationErrors.size()>0)
					txtNUS.setError(Html.fromHtml(getHTMLListFromErrors(validationErrors)));
				else txtNUS.setError(null);
			}
		});		
	}
	
	@Override
	public void setAccountNumberErrors(final List<String> validationErrors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(validationErrors.size()>0)
					txtAccountNumber.setError(Html.fromHtml(getHTMLListFromErrors(validationErrors)));
				else txtAccountNumber.setError(null);
			}
		});		
	}
	
	@Override
	public String getNUS() {
		return txtNUS.getText().toString();
	}

	@Override
	public String getAccountNumber() {
		return txtAccountNumber.getText().toString().replace("-", "").trim();
	}
	

	@Override
	public String getIMEI() {
		return ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	@Override
	public String getPhoneNumber() {
		String phoneNumber = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		return phoneNumber==null?"":phoneNumber;
	}
	
	@Override
	public String getNUSValidationRules() {
		return txtNUS.getTag().toString();
	}

	@Override
	public String getAccountNumberValidationRules() {
		return txtAccountNumber.getTag().toString();
	}
	
	//#endregion
	
	public void btnRegisterAccountClick(View view)
	{
		presenter.processAccountData();
	}
	
}
