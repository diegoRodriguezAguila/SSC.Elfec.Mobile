package com.elfec.ssc.view;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.view.adapters.NotificationAdapter;
import com.elfec.ssc.view.animations.HeightAnimation;

public class Notifications extends ActionBarActivity {

	public enum ExpandStatus {
		COLLAPSED, HALF, FULL
	};

	private ExpandStatus outageStatus;
	private ExpandStatus accountsStatus;
	private int halfSize = -1;
	private int fullSize = -1;
	private TextView titleNotifications;
	private CheckBox outageGroup;
	private ListView outageListView;
	private CheckBox accountsGroup;
	private ListView accountsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		titleNotifications = (TextView) findViewById(R.id.notifications_title);
		outageGroup = (CheckBox) findViewById(R.id.outage_group);
		outageListView = (ListView) findViewById(R.id.outage_listview);
		accountsGroup = (CheckBox) findViewById(R.id.accounts_group);
		accountsListView = (ListView) findViewById(R.id.accounts_listview);
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
		List<Notification> notifs = Notification.getAccountNotifications();
		outageListView.setAdapter(new NotificationAdapter(this, R.layout.notification_list_item, notifs));
		accountsListView.setAdapter(new NotificationAdapter(this, R.layout.notification_list_item, notifs));
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
		changeListViewHeight(outageListView, outageStatus);
		changeListViewHeight(accountsListView, accountsStatus);
	}

	public void changeListViewHeight(ListView wichList, ExpandStatus expandSize) {
		int selectedSize = -1;
		if (expandSize == ExpandStatus.COLLAPSED)
			selectedSize = 0;
		if (expandSize == ExpandStatus.HALF)
			selectedSize = halfSize;
		if (expandSize == ExpandStatus.FULL)
			selectedSize = fullSize;
		HeightAnimation heightAnim = new HeightAnimation(wichList,
				wichList.getHeight(), selectedSize);
		heightAnim.setDuration(500);
		wichList.startAnimation(heightAnim);
	}

}