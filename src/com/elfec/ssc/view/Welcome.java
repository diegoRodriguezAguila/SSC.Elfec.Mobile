package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.presenter.WelcomePresenter;
import com.elfec.ssc.presenter.views.IWelcome;
import com.elfec.ssc.view.controls.AccountPickerDialogService;
import com.elfec.ssc.view.controls.events.IAccountPicked;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Welcome extends ActionBarActivity implements IWelcome {

	private WelcomePresenter presenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		presenter = new WelcomePresenter(this);
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
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	 @Override
		public void onBackPressed() {
		    finish();//go back to the previous Activity
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void btnSelectAccountClick(View view)
	{
		AccountPickerDialogService.instanceService(this, new IAccountPicked() {				
			@Override
			public void onAccountPicked(String gmail) {
				presenter.handlePickedGmailAccount(gmail);
			}
			@Override
			public void onPickedCanceled() {}		
		}).show();
	}
	
	public void btnDeclineAccountClick(View view)
	{
		goToMainMenu();
		getPreferences().setAppAlreadyUsed();
	}

	//#region Interface Methods
	
	@Override
	public void goToMainMenu() {
		Intent i = new Intent(Welcome.this, MainMenu.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out); 
		Welcome.this.finish();
	}

	@Override
	public PreferencesManager getPreferences() {
		return new PreferencesManager(getApplicationContext());
	}
	
	//#endregion
}
