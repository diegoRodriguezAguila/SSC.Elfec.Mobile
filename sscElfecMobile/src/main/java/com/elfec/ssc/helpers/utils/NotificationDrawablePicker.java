package com.elfec.ssc.helpers.utils;

import android.support.annotation.DrawableRes;

import com.elfec.ssc.R;
import com.elfec.ssc.model.enums.NotificationKey;

/**
 * Clase que ayuda a seleccionar el correcto drawable con relación a la key de
 * las notificaciones
 * 
 * @author drodriguez
 *
 */
public class NotificationDrawablePicker {
	/**
	 * Obtiene el drawable que corresponde a la key
	 * 
	 * @return Drawable int
	 */
	@DrawableRes
	public static int getDrawable(NotificationKey key) {
		switch (key) {
		case NEW_ACCOUNT:
			return R.drawable.notif_new_account;
		case ACCOUNT_DELETED:
			return R.drawable.notif_account_deleted;
		case SCHEDULED_OUTAGE:
			return R.drawable.notif_scheduled_outage;
		case INCIDENTAL_OUTAGE:
			return R.drawable.notif_incidental_outage;
		case NONPAYMENT_OUTAGE:
			return R.drawable.notif_nonpayment_outage;
		case MISCELLANEOUS:
			return R.drawable.add_account_mini;
		case EXPIRED_DEBT:
			return R.drawable.notif_expired_debt;
		default:
			return R.drawable.add_account_mini;
		}
	}
}
