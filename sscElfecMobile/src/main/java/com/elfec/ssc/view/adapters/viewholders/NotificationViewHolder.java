package com.elfec.ssc.view.adapters.viewholders;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.view.adapters.ExpandableItem;

/**
 * ViewHolder para notificaciones
 * 
 * @author drodriguez
 *
 */
public class NotificationViewHolder {
	public TextView lblMessage;
	public ImageView imgNotification;
	private TextView lblTitle;
	private TextView lblDate;
	private TextView lblHour;

	public NotificationViewHolder(View convertView) {
		lblMessage = (TextView) convertView
				.findViewById(R.id.list_item_notification_message);
		imgNotification = (ImageView) convertView
				.findViewById(R.id.notification_image);
		lblTitle = (TextView) convertView
				.findViewById(R.id.list_item_notification_title);
		lblDate = (TextView) convertView
				.findViewById(R.id.list_item_notification_date);
		lblHour = (TextView) convertView
				.findViewById(R.id.list_item_notification_time);
	}

	/**
	 * Asigna la información a los campos de la vista
	 * 
	 * @param notification
	 */
	public void bindNotification(Notification notification,
			ExpandableItem expandedStatus, Drawable image) {
		int endSize = expandedStatus.isExpanded() ? expandedStatus
				.getExpandedSize() : 0;
		lblMessage.getLayoutParams().height = endSize;
		lblMessage.setText(Html.fromHtml(notification.getContent()));
		imgNotification.setImageDrawable(image);
		lblTitle.setText(Html.fromHtml(notification.getTitle()));
		lblDate.setText(notification.getInsertDate().toString("dd MMM yyyy"));
		lblHour.setText(notification.getInsertDate().toString("HH:mm"));
	}
}
