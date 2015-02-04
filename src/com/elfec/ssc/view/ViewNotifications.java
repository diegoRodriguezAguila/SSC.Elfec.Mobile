package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.ElfecNotificationManager;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationType;
import com.elfec.ssc.presenter.ViewNotificationsPresenter;
import com.elfec.ssc.presenter.views.IViewNotifications;
import com.elfec.ssc.view.adapters.NotificationAdapter;
import com.elfec.ssc.view.animations.HeightAnimation;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;

public class ViewNotifications extends ActionBarActivity implements IViewNotifications {

	public enum ExpandStatus {
		COLLAPSED, HALF, FULL
	};

	private ViewNotificationsPresenter presenter;
	private ExpandStatus outageStatus;
	private ExpandStatus accountsStatus;
	private int halfSize = -1;
	private int fullSize = -1;
	private TextView titleNotifications;
	private CheckBox outageGroup;
	private XListView outageListView;
	private CheckBox accountsGroup;
	private XListView accountsListView;
	
	private LinearLayout outageLayout;
	private LinearLayout accountsLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		presenter = new ViewNotificationsPresenter(this);
		
		titleNotifications = (TextView) findViewById(R.id.notifications_title);
		outageGroup = (CheckBox) findViewById(R.id.outage_group);
		outageListView = (XListView) findViewById(R.id.outage_listview);
		accountsGroup = (CheckBox) findViewById(R.id.accounts_group);
		accountsListView = (XListView) findViewById(R.id.accounts_listview);
		outageLayout = (LinearLayout) findViewById(R.id.outage_listlayout);
		accountsLayout = (LinearLayout) findViewById(R.id.accounts_listlayout);
		outageStatus = ExpandStatus.COLLAPSED;
		accountsStatus = ExpandStatus.COLLAPSED;
		outageListView.setPullLoadEnable(false);
		accountsListView.setPullLoadEnable(false);
		initializeListListeners();
		presenter.loadNotifications();
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

	/**
	 * Define las constantes de halfSize y fullSize para las listas
	 */
	private void defineSizes() {
		if (halfSize == -1 || fullSize == -1) {
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			ActionBar actionBar = getSupportActionBar();
			int screenHeight = displayMetrics.heightPixels;
			int actionBarHeight = actionBar.getHeight();
			int titleHeight = titleNotifications.getHeight();
			float paddingTop = (6 * displayMetrics.density);
			float paddingBottom = (16 * displayMetrics.density);
			float extraMarginFull = (15 * displayMetrics.density);
			halfSize = (int) (((screenHeight - actionBarHeight - titleHeight
					- paddingBottom - paddingTop) / 2) - ((outageGroup
					.getHeight() * 1.5)));
			fullSize = (int) ((screenHeight - actionBarHeight - titleHeight
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
	
	public void deleteOutageNotifications(View view)
	{
		ElfecNotificationManager.removeAllNotifications(NotificationType.OUTAGE);
		List<Notification> notifs = Notification.getNotificationsByType(NotificationType.ACCOUNT);
		outageListView.setAdapter(new NotificationAdapter(this, R.layout.notification_list_item, notifs));		
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
		heightAnim.setAnimationListener(new AnimationListener() {			
			@Override
			public void onAnimationStart(Animation anim) {}		
			@Override
			public void onAnimationRepeat(Animation anim) {}			
			@Override
			public void onAnimationEnd(Animation anim) {
				if (expandSize != ExpandStatus.COLLAPSED)
				{
					validateLoadMoreOption(concordantListView);
				}
			}
		});
		wichLayout.startAnimation(heightAnim);
	}
	
	/**
	 * Valida si es que es necesario habilitar o deshabilitar el boton de cargar mas dependiendo
	 * si es que todos los items caben en la pantalla o no
	 * @param concordantListView
	 */
	private void validateLoadMoreOption(
			final XListView concordantListView) {
		int firstVisible = concordantListView.getFirstVisiblePosition();
		int lastVisible = concordantListView.getLastVisiblePosition();
		int listLastVisible = concordantListView.getAdapter().getCount();
		if(firstVisible==0 && lastVisible+1==listLastVisible)
		{
			concordantListView.setPullLoadEnable(false);
		}
		else concordantListView.setPullLoadEnable(true);
	}

	/**
	 * Asigna los refresh y load more listeners para ambas listas
	 */
	private void setOnRefreshAndLoadListeners() {
		outageListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				Toast.makeText(ViewNotifications.this, "Refresheando wn", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLoadMore() {
				Toast.makeText(ViewNotifications.this, "Cargando mas wn", Toast.LENGTH_SHORT).show();
			}
		});
		accountsListView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				Toast.makeText(ViewNotifications.this, "Refresheando wn", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLoadMore() {
				Toast.makeText(ViewNotifications.this, "Cargando mas wn", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//#region Interface Methods
	@Override
	public void showOutageList(final List<Notification> outageNotifications) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				outageListView.setAdapter(new NotificationAdapter(ViewNotifications.this, R.layout.notification_list_item, outageNotifications));	
			}
		});
	}

	@Override
	public void showAccountsList(final List<Notification> accountNotifications) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				accountsListView.setAdapter(new NotificationAdapter(ViewNotifications.this, R.layout.notification_list_item, accountNotifications));	
			}
		});
	}

	@Override
	public void setMoreOutageNotificationsEnabled(boolean enabled) {
		outageListView.setPullLoadEnable(enabled);
	}

	@Override
	public void setMoreAcccountNotificationsEnabled(boolean enabled) {
		accountsListView.setPullLoadEnable(enabled);
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
	
	//#endregion
}
