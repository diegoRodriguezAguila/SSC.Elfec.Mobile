package com.elfec.ssc.view.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.view.animations.HeightAnimation;

public class NotificationAdapter extends ArrayAdapter<Notification> {

	private List<AdapterItemWrapper<Notification>> notifications;
	private int resource;
	private LayoutInflater inflater = null;
	public NotificationAdapter(Context context, int resource,
			List<Notification> notifications) {
		super(context, resource, notifications);
		this.notifications = new ArrayList<AdapterItemWrapper<Notification>>();
		for(Notification notif : notifications)
		{
			this.notifications.add(new AdapterItemWrapper<Notification>(notif,R.drawable.notif_account_deleted));
		}
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return notifications.size();
	}

	@Override
	public Notification getItem(int position) {
		return notifications.get(position).getWrappedObject();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public void addAll(Collection<? extends Notification> collection) {
		for(Notification notif : collection)
		{
			this.notifications.add(new AdapterItemWrapper<Notification>(notif,R.drawable.notif_new_account));
		}
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AdapterItemWrapper<Notification> wrappedNotif = notifications.get(position);
		if(convertView==null || Integer.parseInt(convertView.getTag().toString())!=position)
		{
			convertView = inflater.inflate(resource, null);
			convertView.setTag(position);
			setClickListenerToItem(wrappedNotif, convertView,
					((TextView) convertView.findViewById(R.id.list_item_notification_message)), position);
		}
		TextView txtNotifMessage = (TextView) convertView.findViewById(R.id.list_item_notification_message);
		int endSize = 0;
		if(wrappedNotif.isExpanded())
		{
			endSize =  wrappedNotif.getExpandedSize();
		}
		txtNotifMessage.getLayoutParams().height = endSize;
		Notification notification = wrappedNotif.getWrappedObject();
		((ImageView) convertView.findViewById(R.id.notification_image)).setImageDrawable(
				getContext().getResources().getDrawable(wrappedNotif.getImageResourceId()));
		((TextView) convertView.findViewById(R.id.list_item_notification_title)).setText(notification.getTitle());
		txtNotifMessage.setText(notification.getContent());
		((TextView) convertView.findViewById(R.id.list_item_notification_date)).setText(notification.getInsertDate().toString("dd MMM yyyy"));
		((TextView) convertView.findViewById(R.id.list_item_notification_time)).setText(notification.getInsertDate().toString("HH:mm"));

		return convertView;
	}
	
	private void setClickListenerToItem(
			final AdapterItemWrapper<Notification> wrappedNotif,
			final View convertView, final TextView txtNotifMessage, final int pos) {		
				txtNotifMessage.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
				wrappedNotif.setExpandedSize(txtNotifMessage.getMeasuredHeight());
				convertView.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						int initSize = 0;
						int endSize = wrappedNotif.getExpandedSize();
						if(wrappedNotif.isExpanded())
						{
							initSize = wrappedNotif.getExpandedSize();
							endSize = 0;
						}
						wrappedNotif.setExpanded(!wrappedNotif.isExpanded());
						HeightAnimation heightAnim = new HeightAnimation(txtNotifMessage,
								initSize, endSize);
						heightAnim.setDuration(250);
						txtNotifMessage.startAnimation(heightAnim);
					}
				});
	}

}
