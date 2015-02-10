package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.helpers.PreferencesManager;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.view.adapters.ViewAccountsAdapter;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.maps.internal.l;

public class ViewAccounts extends ActionBarActivity implements IViewAccounts {

	private ViewAccountsPresenter presenter;
	private XListView accountsListView;
	private AlertDialog waitingWSDialog;
	private boolean isRefresh;
	@Override
	protected void onResume()
	{
		super.onResume();
		ViewPresenterManager.setPresenter(presenter);
		isRefresh=false;
	    presenter.gatherAccounts();
	}
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		ViewPresenterManager.setPresenter(null);
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);
        presenter = new ViewAccountsPresenter(this);
        accountsListView=(XListView)findViewById(R.id.accounts_list);
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
        accountsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int position, long arg3) {
				dialogRemove(position);
				return false;
			}
        }); 
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
				ViewAccountsAdapter adapter=new ViewAccountsAdapter(ViewAccounts.this, R.layout.view_accounts_row, result);
				accountsListView.setAdapter(adapter);
				if(result.size()>0)
				{
					findViewById(R.id.layout_how_to_add_accounts).setVisibility(View.GONE);
					findViewById(R.id.lbl_an_account).setVisibility(View.GONE);
					findViewById(R.id.lbl_no_accounts_registered).setVisibility(View.GONE);
					accountsListView.setVisibility(View.VISIBLE);
				}
				else
				{
					findViewById(R.id.layout_how_to_add_accounts).setVisibility(View.VISIBLE);
					findViewById(R.id.lbl_an_account).setVisibility(View.VISIBLE);
					findViewById(R.id.lbl_no_accounts_registered).setVisibility(View.VISIBLE);
					accountsListView.setVisibility(View.GONE);
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
				SuperToast.cancelAllSuperToasts();
				SuperToast.create(ViewAccounts.this, R.string.account_successfully_deleted, SuperToast.Duration.LONG, 
					    Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();	
			}
		});
	}

	@Override
	public void displayErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				StringBuilder msg = new StringBuilder();
				int size = errors.size();
				if(size==1)
					msg.append(errors.get(0).getMessage()).toString();
				else
				{
					for (int i = 0; i < size; i++) {
						msg.append("● ").append(errors.get(i).getMessage());
						msg.append((i<size-1?"\n":""));
					}
				}
				AlertDialogPro.Builder builder = new AlertDialogPro.Builder(ViewAccounts.this);
				builder.setTitle(R.string.errors_on_delete_account_title)
				.setMessage(msg)
				.setPositiveButton(R.string.btn_ok, null)
				.show();
			}
		});
	}
	
	@Override
	public void dialogRemove(final int position)
    {
		(new AlertDialogPro.Builder(this)).setTitle(R.string.delete_account_title)
        .setMessage(R.string.delete_account_msg)
        .setPositiveButton(R.string.btn_yes, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Account account=(Account)accountsListView.getAdapter().getItem(position);
				ShowWaitingWS();
				presenter.invokeRemoveAccountWS(account.getNUS());
			}
		}).setNegativeButton(R.string.btn_no, null).show();
    }
	@Override
	public void ShowWaitingWS() {
		runOnUiThread(new Runnable() {
			@Override
			public void run(){
				waitingWSDialog = new ProgressDialogPro(ViewAccounts.this, R.style.Theme_FlavoredMaterialLight);
				waitingWSDialog.setMessage(ViewAccounts.this.getResources().getString(R.string.waiting_msg));
				waitingWSDialog.setCancelable(false);
				waitingWSDialog.setCanceledOnTouchOutside(false);
				waitingWSDialog.show();
			}
		});
	}
	@Override
	public void hideWSWaiting() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(waitingWSDialog!=null)
					waitingWSDialog.dismiss();
				accountsListView.stopRefresh();
			}
		});
	}
	@Override
	public void showViewAccountsErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				StringBuilder msg = new StringBuilder();
				int size = errors.size();
				if(size==1)
					msg.append(errors.get(0).getMessage()).toString();
				else
				{
					for (int i = 0; i < size; i++) {
						msg.append("● ").append(errors.get(i).getMessage());
						msg.append((i<size-1?"\n":""));
					}
				}
				AlertDialogPro.Builder builder = new AlertDialogPro.Builder(ViewAccounts.this);
				builder.setTitle(R.string.errors_on_download_accounts_title)
				.setMessage(msg)
				.setPositiveButton(R.string.btn_ok, null)
				.show();
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
	
}
