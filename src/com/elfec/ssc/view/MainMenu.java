package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.presenter.MainMenuPresenter;
import com.elfec.ssc.presenter.views.IMainMenu;
import com.elfec.ssc.view.controls.AccountPickerDialogService;
import com.elfec.ssc.view.controls.events.OnAccountPicked;

public class MainMenu extends ActionBarActivity implements IMainMenu {

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
	
	private long lastClickTime = 0;
	
	public void btnAccountsClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 600){
			presenter.verifyAccountsRequirements();
        }
        lastClickTime = SystemClock.elapsedRealtime();

	}
	
	public void btnLocationServicesClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, LocationServices.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnNotificationsClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, ViewNotifications.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnContactsClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			Intent i = new Intent(MainMenu.this, Contacts.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}
	
	public void btnSwitchClientClick(View view)
	{
		if (SystemClock.elapsedRealtime() - lastClickTime > 1000){
			showAccountPickerDialog("ssc.elfec@gmail.com");
		}
        lastClickTime = SystemClock.elapsedRealtime();
	}

	//#region Interface Methods
	
	@Override
	public void goToViewAccounts() {
		Intent i = new Intent(MainMenu.this, ViewAccounts.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

	@Override
	public PreferencesManager getPreferences() {
		return new PreferencesManager(getApplicationContext());
	}

	@Override
	public void warnUserHasNoAccounts() {
		(new AlertDialogPro.Builder(this)).setTitle(R.string.no_gmail_account_title)
		.setMessage(R.string.no_gmail_account_message)
        .setPositiveButton(R.string.btn_ok, new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {	
				showAccountPickerDialog();
			}		
		})
		.setNegativeButton(R.string.btn_cancel, null)
		.show();
	}
	
	public void showAccountPickerDialog(String activeClientGmail)
	{
		AccountPickerDialogService.instanceService(this, new OnAccountPicked() {				
			@Override
			public void onAccountPicked(String gmail) {
				presenter.handlePickedGmailAccount(gmail);
			}
			@Override
			public void onPickedCanceled() {}		
		},activeClientGmail).show();
	}
	
	public void showAccountPickerDialog()
	{
		showAccountPickerDialog(null);
	}
	
	//#endregion
}
