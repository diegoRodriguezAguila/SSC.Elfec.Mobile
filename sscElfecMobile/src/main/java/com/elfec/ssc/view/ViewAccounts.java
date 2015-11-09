package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.security.PreferencesManager;
import com.elfec.ssc.view.adapters.ViewAccountsAdapter;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

public class ViewAccounts extends AppCompatActivity implements IViewAccounts {

	private ViewAccountsPresenter presenter;

	private boolean isDestroyed;

	private Toolbar toolbar;
	private XListView accountsListView;
	private AlertDialog waitingWSDialog;
	private boolean isRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_accounts);
		isDestroyed = false;
		presenter = new ViewAccountsPresenter(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout
															// to the toolbar
															// object
		setSupportActionBar(toolbar);
		((TextView) toolbar.findViewById(R.id.toolbar_title))
				.setText(R.string.view_accounts_title);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		accountsListView = (XListView) findViewById(R.id.accounts_list);
		accountsListView.setPullLoadEnable(false);
		accountsListView.setPullRefreshEnable(true);
		accountsListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				presenter.getAllServiceAccounts();
			}

			@Override
			public void onLoadMore() {

			}
		});
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		accountsListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View view, int position, long arg3) {
						dialogRemove(position);
						return true;
					}
				});
		accountsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				if (ButtonClicksHelper.canClickButton()) {
					Intent i = new Intent(ViewAccounts.this,
							ViewAccountDetails.class);
					Long id = ((Account) adapter.getItemAtPosition(pos))
							.getId();
					i.putExtra(ViewAccountDetails.SELECTED_ACCOUNT_ID, id);
					startActivity(i);
					overridePendingTransition(R.anim.slide_left_in,
							R.anim.slide_left_out);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		ViewPresenterManager.setPresenter(presenter);
		isRefresh = false;
		presenter.gatherAccounts();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroyed = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		ViewPresenterManager.setPresenter(null);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public String getIMEI() {
		return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_accounts, menu);
		return true;
	}

	public void btnRegisterAccountClick(View view) {
		if (ButtonClicksHelper.canClickButton()) {
			Intent i = new Intent(ViewAccounts.this, RegisterAccount.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		}
	}

	@Override
	public void onBackPressed() {
		finish();// go back to the previous Activity
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	@Override
	public void showAccounts(final List<com.elfec.ssc.model.Account> result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!isDestroyed) {
					if (result != null && result.size() > 0) {
						ViewAccountsAdapter adapter = new ViewAccountsAdapter(
								ViewAccounts.this, R.layout.view_accounts_row,
								result);
						accountsListView.setAdapter(adapter);
						findViewById(R.id.layout_how_to_add_accounts)
								.setVisibility(View.GONE);
						findViewById(R.id.lbl_an_account).setVisibility(
								View.GONE);
						findViewById(R.id.lbl_no_accounts_registered)
								.setVisibility(View.GONE);
						accountsListView.setVisibility(View.VISIBLE);
					} else {
						findViewById(R.id.layout_how_to_add_accounts)
								.setVisibility(View.VISIBLE);
						findViewById(R.id.lbl_an_account).setVisibility(
								View.VISIBLE);
						findViewById(R.id.lbl_no_accounts_registered)
								.setVisibility(View.VISIBLE);
						accountsListView.setVisibility(View.GONE);
					}
				}
			}
		});
	}

	@Override
	public PreferencesManager getPreferences() {
		return new PreferencesManager(getApplicationContext());
	}

	@Override
	public void refreshAccounts() {
		presenter.gatherAccounts();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!isDestroyed) {
					SuperToast.cancelAllSuperToasts();
					SuperToast.create(
							ViewAccounts.this,
							R.string.account_successfully_deleted,
							SuperToast.Duration.LONG,
							Style.getStyle(Style.BLUE,
									SuperToast.Animations.FADE)).show();
				}
			}
		});
	}

	@Override
	public void displayErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!isDestroyed && errors.size() > 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ViewAccounts.this);
					builder.setTitle(R.string.errors_on_delete_account_title)
							.setMessage(
									MessageListFormatter
											.fotmatHTMLFromErrors(errors))
							.setPositiveButton(R.string.btn_ok, null).show();
				}
			}
		});
	}

	@Override
	public void dialogRemove(final int position) {
		(new AlertDialog.Builder(this))
				.setTitle(R.string.delete_account_title)
				.setMessage(R.string.delete_account_msg)
				.setPositiveButton(R.string.btn_confirm, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Account account = (Account) accountsListView
								.getAdapter().getItem(position);
						showWSWaiting();
						presenter.invokeRemoveAccountWS(account.getNUS());
					}
				}).setNegativeButton(R.string.btn_cancel, null).show();
	}

	@Override
	public void showWSWaiting() {
		/*
		 * runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run(){ waitingWSDialog = new
		 * ProgressDialogPro(ViewAccounts.this,
		 * R.style.Theme_FlavoredMaterialLight);
		 * waitingWSDialog.setMessage(ViewAccounts
		 * .this.getResources().getString(R.string.waiting_msg));
		 * waitingWSDialog.setCancelable(false);
		 * waitingWSDialog.setCanceledOnTouchOutside(false);
		 * waitingWSDialog.show(); } });
		 */
	}

	@Override
	public void hideWSWaiting() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!isDestroyed) {
					if (waitingWSDialog != null)
						waitingWSDialog.dismiss();
					accountsListView.stopRefresh();
					findViewById(R.id.loading_view_accounts).setVisibility(
							View.GONE);
					findViewById(R.id.lbl_loading_accounts).setVisibility(
							View.GONE);
				}

			}
		});
	}

	@Override
	public void showViewAccountsErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!isDestroyed && errors.size() > 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ViewAccounts.this);
					builder.setTitle(R.string.errors_on_download_accounts_title)
							.setMessage(
									MessageListFormatter
											.fotmatHTMLFromErrors(errors))
							.setPositiveButton(R.string.btn_ok, null).show();
				}
			}
		});
	}

	@Override
	public GCMTokenRequester getGCMTokenRequester() {
		return new GCMTokenRequester(this);
	}

	@Override
	public boolean isRefreshed() {
		return isRefresh;
	}

	@Override
	public WSTokenRequester getWSTokenRequester() {
		return new WSTokenRequester(this);
	}

}
