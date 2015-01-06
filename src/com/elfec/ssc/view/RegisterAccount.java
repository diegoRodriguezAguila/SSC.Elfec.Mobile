package com.elfec.ssc.view;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.RegisterAccountPresenter;
import com.elfec.ssc.presenter.views.IRegisterAccount;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    
    //#region  Interface IRegisterAccount methods

	@Override
	public String getNUS() {
		return txtNUS.getText().toString();
	}

	@Override
	public String getAccountNumber() {
		return txtAccountNumber.getText().toString();
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
	
	//#endregion
	
	public void btnRegisterAccountClick(View view)
	{
		presenter.processAccountData();
	}
	
}
