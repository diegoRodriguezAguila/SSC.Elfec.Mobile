package com.elfec.ssc.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.ssc.helpers.ui.DrawableCache;
import com.elfec.ssc.helpers.utils.NotificationDrawablePicker;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.view.adapters.viewholders.NotificationViewHolder;
import com.elfec.ssc.view.animations.HeightAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

	private List<ExpandableItem> mExpandedStatuses;
	private List<Notification> mNotifications;
	private int mResourceId;
	private DrawableCache mDrawableCache;
	private LayoutInflater mInflater = null;

	public NotificationAdapter(Context context, @LayoutRes int resource,
			List<Notification> notifications) {
		super(context, resource, notifications);
		mInflater = LayoutInflater.from(context);
		int size = notifications.size();
		mExpandedStatuses = new ArrayList<ExpandableItem>(size);
		for (int i = 0; i < size; i++) {
			mExpandedStatuses.add(new ExpandableItem());
		}
		this.mResourceId = resource;
		this.mNotifications = notifications;
		this.mDrawableCache = new DrawableCache(context);
	}

	@Override
	public Notification getItem(int position) {
		return mNotifications.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Agrega una notificación al frente y elimina la ultima
	 * 
	 * @param notification
	 * @param removeLast
	 */
	public void addNewNotificationUpdate(Notification notification,
			boolean removeLast) {
		mNotifications.add(0, notification);
		mExpandedStatuses.add(0, new ExpandableItem());
		if (removeLast) {
			int delPos = mNotifications.size() - 1;
			mNotifications.remove(delPos);
			mExpandedStatuses.remove(delPos);
		}
		notifyDataSetChanged();
	}

	@Override
	public void addAll(Collection<? extends Notification> collection) {
		mNotifications.addAll(collection);
		int size = collection.size();
		for (int i = 0; i < size; i++) {
			this.mExpandedStatuses.add(new ExpandableItem());
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(mResourceId, parent, false);
			viewHolder = new NotificationViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else
			viewHolder = (NotificationViewHolder) convertView.getTag();
		final ExpandableItem expandedStatus = mExpandedStatuses.get(position);
		final Notification notif = getItem(position);
		viewHolder.bindNotification(notif, expandedStatus, mDrawableCache
				.getDrawable(NotificationDrawablePicker.getDrawable(notif
						.getKey())));
		final TextView txtNotifMessage = viewHolder.lblMessage;
		txtNotifMessage.measure(MeasureSpec.UNSPECIFIED,
				MeasureSpec.UNSPECIFIED);
		setGlobalLayoutListener(expandedStatus, txtNotifMessage);
		setClickListenerToItem(expandedStatus, convertView,
				viewHolder.lblMessage, position);
		return convertView;
	}

	private void setGlobalLayoutListener(final ExpandableItem expandedStatus,
			final TextView txtNotifMessage) {
		txtNotifMessage.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@SuppressLint("NewApi")
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						expandedStatus.setExpandedSize(txtNotifMessage
								.getLineCount()
								* txtNotifMessage.getLineHeight());
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
							txtNotifMessage.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							txtNotifMessage.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
					}
				});
	}

	private void setClickListenerToItem(final ExpandableItem expandedStatus,
			final View convertView, final TextView txtNotifMessage,
			final int pos) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int initSize = 0;
				int endSize = expandedStatus.getExpandedSize();
				if (expandedStatus.isExpanded()) {
					initSize = expandedStatus.getExpandedSize();
					endSize = 0;
				}
				expandedStatus.setExpanded(!expandedStatus.isExpanded());
				HeightAnimation heightAnim = new HeightAnimation(
						txtNotifMessage, initSize, endSize);
				heightAnim.setDuration(250);
				txtNotifMessage.startAnimation(heightAnim);
			}
		});
	}

}
