package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.presenter.ViewNotificationsPresenter;
import com.elfec.ssc.presenter.views.IViewNotifications;
import com.elfec.ssc.view.adapters.NotificationAdapter;
import com.elfec.ssc.view.animations.HeightAnimation;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;

public class ViewNotifications extends AppCompatActivity implements IViewNotifications {

	public enum ExpandStatus {
		COLLAPSED, HALF, FULL
	};

	private ViewNotificationsPresenter presenter;
	
	private Toolbar toolbar;
	private ExpandStatus outageStatus;
	private ExpandStatus accountsStatus;
	private int halfSize = -1;
	private int fullSize = -1;
	private CheckBox outageGroup;
	private XListView outageListView;
	private CheckBox accountsGroup;
	private XListView accountsListView;
	private TextView lblNoOutageNotifications;
	private TextView lblNoAccountNotifications;
	
	private NotificationAdapter outagesAdapter;
	private NotificationAdapter accountsAdapter;
	
	private LinearLayout outageLayout;
	private LinearLayout accountsLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		presenter = new ViewNotificationsPresenter(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar); 
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(R.string.notifications_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		outageGroup = (CheckBox) findViewById(R.id.outage_group);
		outageListView = (XListView) findViewById(R.id.outage_listview);
		accountsGroup = (CheckBox) findViewById(R.id.accounts_group);
		accountsListView = (XListView) findViewById(R.id.accounts_listview);
		outageLayout = (LinearLayout) findViewById(R.id.outage_listlayout);
		accountsLayout = (LinearLayout) findViewById(R.id.accounts_listlayout);
		lblNoOutageNotifications = (TextView) findViewById(R.id.lbl_no_outage_notifications);
		lblNoAccountNotifications = (TextView) findViewById(R.id.lbl_no_account_notifications);
		outageStatus = ExpandStatus.COLLAPSED;
		accountsStatus = ExpandStatus.COLLAPSED;
		outageListView.setPullLoadEnable(false);
		accountsListView.setPullLoadEnable(false);
		initializeListListeners();
	}

	@Override
	public void onBackPressed() {
		finish();// go back to the previous Activity
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		ViewPresenterManager.setPresenter(null);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		presenter.loadNotifications();
		ViewPresenterManager.setPresenter(presenter);
	}

	/**
	 * Define las constantes de halfSize y fullSize para las listas
	 */
	private void defineSizes() {
		if (halfSize == -1 || fullSize == -1) {
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			ActionBar actionBar = getSupportActionBar();
			int screenHeight = displayMetrics.heightPixels;
			int actionBarHeight = actionBar.getHeight();
			float paddingTop = (6 * displayMetrics.density);
			float paddingBottom = (16 * displayMetrics.density);
			float extraMarginFull = (15 * displayMetrics.density);
			halfSize = (int) (((screenHeight - actionBarHeight
					- paddingBottom - paddingTop) / 2) - ((outageGroup
					.getHeight() * 1.5)));
			fullSize = (int) ((screenHeight - actionBarHeight
					- paddingBottom - paddingTop)
					- (outageGroup.getHeight() * 2.5) - extraMarginFull);
		}
	}
	
	/**
	 * Inicia un hilo para asignar los listeners a ambas listas
	 */
	private void initializeListListeners() {
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				setOnCheckListeners();	
				setOnRefreshAndLoadListeners();
			}
		});
		thread.start();
	}	
	
	/**
	 * Pone los check listeners para los cortes y las cuentas
	 */
	private void setOnCheckListeners() {
		OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				redefineViewSizes();
			}
		};
		outageGroup.setOnCheckedChangeListener(checkListener);
		accountsGroup.setOnCheckedChangeListener(checkListener);
	}

	/**
	 * según los checkbox marcados distribuye enl a pantalla las dos listas de notificaciones
	 */
	public void redefineViewSizes() {
		defineSizes();
		if (outageGroup.isChecked() && accountsGroup.isChecked()) {
			outageStatus = ExpandStatus.HALF;
			accountsStatus = ExpandStatus.HALF;
		}
		if (!outageGroup.isChecked() && accountsGroup.isChecked()) {
			outageStatus = ExpandStatus.COLLAPSED;
			accountsStatus = ExpandStatus.FULL;
		}
		if (outageGroup.isChecked() && !accountsGroup.isChecked()) {
			outageStatus = ExpandStatus.FULL;
			accountsStatus = ExpandStatus.COLLAPSED;
		}
		if (!outageGroup.isChecked() && !accountsGroup.isChecked()) {
			outageStatus = ExpandStatus.COLLAPSED;
			accountsStatus = ExpandStatus.COLLAPSED;
		}
		changeLinearLayoutHeight(outageLayout, outageStatus, outageListView);
		changeLinearLayoutHeight(accountsLayout, accountsStatus, accountsListView);
	}
	
	public void btndeleteOutageNotificationsClick(View view)
	{
		(new AlertDialogPro.Builder(this))
		.setTitle(R.string.delete_notifications_title)
		.setMessage(R.string.delete_outage_notifications_msg)
		.setPositiveButton(R.string.btn_ok, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				presenter.clearOutageNotifications();	
			}
		})
		.setNegativeButton(R.string.btn_cancel, null).show();
	}
	
	public void btndeleteAccountNotificationsClick(View view)
	{
		(new AlertDialogPro.Builder(this))
		.setTitle(R.string.delete_notifications_title)
		.setMessage(R.string.delete_account_notifications_msg)
		.setPositiveButton(R.string.btn_ok, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				presenter.clearAccountNotifications();	
			}
		})
		.setNegativeButton(R.string.btn_cancel, null).show();
	}

	
	/**
	 * Crea una animación para el layout del parámetro
	 * @param wichLayout
	 * @param expandSize
	 * @param concordantListView
	 */
	public void changeLinearLayoutHeight(final LinearLayout wichLayout, final ExpandStatus expandSize, final XListView concordantListView) {
		int selectedSize = -1;
		if (expandSize == ExpandStatus.COLLAPSED)
			selectedSize = 0;
		if (expandSize == ExpandStatus.HALF)
			selectedSize = halfSize;
		if (expandSize == ExpandStatus.FULL)
			selectedSize = fullSize;
		HeightAnimation heightAnim = new HeightAnimation(wichLayout,
				wichLayout.getHeight(), selectedSize);
		heightAnim.setDuration(500);
		wichLayout.startAnimation(heightAnim);
	}
	
	/**
	 * Asigna los refresh y load more listeners para ambas listas
	 */
	private void setOnRefreshAndLoadListeners() {
		outageListView.setXListViewListener(new IXListViewListener() {			
			@Override
			public void onRefresh() {
				presenter.refreshOutageNotifications();
			}
			
			@Override
			public void onLoadMore() {
				presenter.loadMoreOutageNotifications();
			}
		});
		accountsListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				presenter.refreshAccountNotifications();
			}
			
			@Override
			public void onLoadMore() {
				presenter.loadMoreAccountNotifications();
			}
		});
	}
	
	//#region Interface Methods
	@Override
	public void setOutageList(final List<Notification> outageNotifications) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				lblNoOutageNotifications.setVisibility(outageNotifications.size()==0?View.VISIBLE:View.GONE);
				outagesAdapter = new NotificationAdapter(ViewNotifications.this, R.layout.notification_list_item, outageNotifications);
				outageListView.setAdapter(outagesAdapter);	
			}
		});
	}

	@Override
	public void setAccountsList(final List<Notification> accountNotifications) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				lblNoAccountNotifications.setVisibility(accountNotifications.size()==0?View.VISIBLE:View.GONE);
				accountsAdapter = new NotificationAdapter(ViewNotifications.this, R.layout.notification_list_item, accountNotifications);
				accountsListView.setAdapter(accountsAdapter);	
			}
		});
	}
	
	
	@Override
	public void addOutageNotifications(final List<Notification> outageNotifications) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				if(outagesAdapter==null)
					setOutageList(outageNotifications);
				else outagesAdapter.addAll(outageNotifications);
			}
		});
	}

	@Override
	public void addAccountNotifications(final List<Notification> accountNotifications) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				if(accountsAdapter==null)
					setAccountsList(accountNotifications);
				else accountsAdapter.addAll(accountNotifications);
			}
		});
	}

	@Override
	public void setMoreOutageNotificationsEnabled(final boolean enabled) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				outageListView.setPullLoadEnable(enabled);
			}
		});
	}

	@Override
	public void setMoreAcccountNotificationsEnabled(final boolean enabled) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				accountsListView.setPullLoadEnable(enabled);
			}
		});
	}

	@Override
	public void loadAndRefreshOutageFinished() {
		runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				outageListView.stopRefresh();
				outageListView.stopLoadMore();
			}
		});
		System.gc();
	}

	@Override
	public void loadAndRefreshAccountsFinished() {
		runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				accountsListView.stopRefresh();
				accountsListView.stopLoadMore();
			}
		});
		System.gc();
	}

	@Override
	public void hideOutageList() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				outageGroup.setChecked(false);
			}
		});
	}

	@Override
	public void hideAccountsList() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				accountsGroup.setChecked(false);
			}
		});
	}

	@Override
	public void showNewOutageNotificationUpdate(final Notification notif, final boolean removeLast) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				outagesAdapter.addNewNotificationUpdate(notif, removeLast);
				lblNoOutageNotifications.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void showNewAccountNotificationUpdate(final Notification notif, final boolean removeLast) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				accountsAdapter.addNewNotificationUpdate(notif, removeLast);
				lblNoAccountNotifications.setVisibility(View.GONE);
			}
		});
	}
	
	//#endregion
}
