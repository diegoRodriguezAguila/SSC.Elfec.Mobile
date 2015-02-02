package com.elfec.ssc.view.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.elfec.ssc.R;
import com.elfec.ssc.model.Notification;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		AdapterItemWrapper<Notification> wrappedNotif = notifications.get(position);
		if(convertView==null)
		{
			convertView = inflater.inflate(resource, null);
		}
		Notification notification = wrappedNotif.getWrappedObject();
		((TextView) convertView.findViewById(R.id.list_item_notification_title)).setText(notification.getTitle());
		((TextView) convertView.findViewById(R.id.list_item_notification_message)).setText(notification.getContent());

		return convertView;
	}

}
