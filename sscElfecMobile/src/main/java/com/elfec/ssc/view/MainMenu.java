package com.elfec.ssc.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.webservices.SSLConection;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.presenter.MainMenuPresenter;
import com.elfec.ssc.presenter.views.IMainMenu;
import com.elfec.ssc.view.controls.AccountPickerDialogService;
import com.elfec.ssc.view.controls.events.OnAccountPicked;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenu extends AppCompatActivity implements IMainMenu {

	private MainMenuPresenter presenter;
	protected @Bind(R.id.btn_switch_client) ImageButton mBtnSwitchClient;
	protected @Bind(R.id.txt_active_client) TextView mTxtActiveClient;
	protected @Bind(R.id.txt_active_client_info) TextView mTxtActiveClientInfo;
	private String mActiveClientGmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		presenter = new MainMenuPresenter(this);
        ButterKnife.bind(this);
		mBtnSwitchClient.setEnabled(false);
		// getSignature();
		SSLConection.allowSelfSignedElfecSSL(this);
	}

	private void getSignature() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("LA KEY SIGNATURE",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException | NoSuchAlgorithmException ignored) {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mActiveClientGmail == null)
			presenter.loadCurrentClient();
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
		return id == R.id.action_about || super.onOptionsItemSelected(item);
	}

	public void btnAccountsClick(View view) {
		if (ButtonClicksHelper.canClickButton()) {
			presenter.verifyAccountsRequirements();
		}
	}

	public void btnLocationServicesClick(View view) {
		if (ButtonClicksHelper.canClickButton()) {
			Intent i = new Intent(MainMenu.this, LocationServices.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		}
	}

	public void btnNotificationsClick(View view) {
		if (ButtonClicksHelper.canClickButton()) {
			Intent i = new Intent(MainMenu.this, ViewNotifications.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		}
	}

	public void btnContactsClick(View view) {
		if (ButtonClicksHelper.canClickButton()) {
			Intent i = new Intent(MainMenu.this, Contacts.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		}
	}

	public void btnSwitchClientClick(View view) {
		if (ButtonClicksHelper.canClickButton()) {
			(new AlertDialog.Builder(this))
					.setTitle(R.string.switch_client_title)
					.setMessage(R.string.switch_client_msg)
					.setPositiveButton(R.string.btn_ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							showAccountPickerDialog(mActiveClientGmail);
						}
					}).setNegativeButton(R.string.btn_cancel, null).show();
		}
	}

	// #region Interface Methods

	@Override
	public void goToViewAccounts() {
		Intent i = new Intent(MainMenu.this, ViewAccounts.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}


	@Override
	public void warnUserHasNoAccounts() {
		(new AlertDialog.Builder(this))
				.setTitle(R.string.no_gmail_account_title)
				.setMessage(R.string.no_gmail_account_message)
				.setPositiveButton(R.string.btn_ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showAccountPickerDialog();
					}
				}).setNegativeButton(R.string.btn_cancel, null).show();
	}

	public void showAccountPickerDialog(String activeClientGmail) {
		new AccountPickerDialogService (this, new OnAccountPicked() {
			@Override
			public void onAccountPicked(String gmail) {
				presenter.handlePickedGmailAccount(gmail);
			}

			@Override
			public void onPickedCanceled() {
			}
		}, activeClientGmail).show();
	}

	public void showAccountPickerDialog() {
		showAccountPickerDialog(null);
	}

	@Override
	public void setCurrentClient(String gmail) {
		mActiveClientGmail = gmail;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mActiveClientGmail != null) {
					mTxtActiveClientInfo.setText(R.string.lbl_current_client);
					mTxtActiveClient.setVisibility(View.VISIBLE);
					mTxtActiveClient.setText(mActiveClientGmail);
					mBtnSwitchClient.setEnabled(true);
				} else
					mTxtActiveClientInfo.setText(R.string.lbl_no_active_client);
			}
		});
	}

	// #endregion
}
