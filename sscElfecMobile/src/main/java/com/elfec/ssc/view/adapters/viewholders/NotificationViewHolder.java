package com.elfec.ssc.view.adapters.viewholders;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.view.adapters.ExpandableItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ViewHolder para notificaciones
 * 
 * @author drodriguez
 *
 */
public class NotificationViewHolder {
	public @Bind(R.id.list_item_notification_message) TextView lblMessage;
	protected @Bind(R.id.notification_image) ImageView mImgNotification;
	protected @Bind(R.id.list_item_notification_title) TextView mLblTitle;
	protected @Bind(R.id.list_item_notification_date) TextView mLblDate;
	protected @Bind(R.id.list_item_notification_time) TextView mLblHour;

	public NotificationViewHolder(View convertView) {
		ButterKnife.bind(this, convertView);
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
		mImgNotification.setImageDrawable(image);
		mLblTitle.setText(Html.fromHtml(notification.getTitle()));
		mLblDate.setText(notification.getInsertDate().toString("dd MMM yyyy"));
		mLblHour.setText(notification.getInsertDate().toString("HH:mm"));
	}
}
