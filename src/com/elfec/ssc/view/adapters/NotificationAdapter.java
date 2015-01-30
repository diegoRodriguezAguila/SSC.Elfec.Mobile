package com.elfec.ssc.view.adapters;

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

	private List<Notification> notifications;
	private int resource;
	private LayoutInflater inflater = null;
	public NotificationAdapter(Context context, int resource,
			List<Notification> notifications) {
		super(context, resource, notifications);
		this.notifications = notifications;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return notifications.size();
	}

	@Override
	public Notification getItem(int position) {
		return notifications.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		Notification notification = notifications.get(position);
		((TextView) convertView.findViewById(R.id.list_item_notification_title)).setText(notification.getTitle());
		((TextView) convertView.findViewById(R.id.list_item_notification_message)).setText(notification.getContent());
		return convertView;
	}

}
