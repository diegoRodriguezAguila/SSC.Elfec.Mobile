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
import com.elfec.ssc.view.adapters.NotificationAdapter;
import com.elfec.ssc.view.animations.HeightAnimation;
import com.elfec.ssc.view.xlistview.XListView;
import com.elfec.ssc.view.xlistview.XListView.IXListViewListener;

public class ViewNotifications extends ActionBarActivity {

	public enum ExpandStatus {
		COLLAPSED, HALF, FULL
	};

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
		titleNotifications = (TextView) findViewById(R.id.notifications_title);
		outageGroup = (CheckBox) findViewById(R.id.outage_group);
		outageListView = (XListView) findViewById(R.id.outage_listview);
		accountsGroup = (CheckBox) findViewById(R.id.accounts_group);
		accountsListView = (XListView) findViewById(R.id.accounts_listview);
		outageGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				redefineViewSizes();
			}
		});
		accountsGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				redefineViewSizes();
			}
		});
		outageStatus = ExpandStatus.COLLAPSED;
		accountsStatus = ExpandStatus.COLLAPSED;
		outageLayout = (LinearLayout) findViewById(R.id.outage_listlayout);
		accountsLayout = (LinearLayout) findViewById(R.id.accounts_listlayout);
		List<Notification> notifs = Notification.getAccountNotifications();
		outageListView.setPullLoadEnable(false);
		accountsListView.setPullLoadEnable(false);
		outageListView.setAdapter(new NotificationAdapter(this, R.layout.notification_list_item, notifs));
		accountsListView.setAdapter(new NotificationAdapter(this, R.layout.notification_list_item, notifs));
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
		List<Notification> notifs = Notification.getAccountNotifications();
		outageListView.setAdapter(new NotificationAdapter(this, R.layout.notification_list_item, notifs));		
	}
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
}
