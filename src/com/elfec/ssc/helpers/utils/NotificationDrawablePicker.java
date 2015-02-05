package com.elfec.ssc.helpers.utils;

import com.elfec.ssc.R;
import com.elfec.ssc.model.enums.NotificationKey;

/**
 * Clase que ayuda a seleccionar el correcto drawable con relación a la key
 * de las notificaciones
 * @author drodriguez
 *
 */
public class NotificationDrawablePicker {
	/**
	 * Obtiene el drawable que corresponde a la key
	 * @return
	 */
	public static int getDrawable(NotificationKey key)
	{
		switch(key)
		{
			case NEW_ACCOUNT:
			{
				return R.drawable.notif_new_account;
			}
			case ACCOUNT_DELETED:
			{
				return R.drawable.notif_account_deleted;
			}
			case SCHEDULED_OUTAGE:
			{
				
			}
			case INCIDENTAL_OUTAGE:
			{
				
			}
			case NONPAYMENT_OUTAGE:
			{
				
			}
			case UNDEFINDED_KEY:
			{
				return R.drawable.add_account_mini;
			}
			default:
			{
				return R.drawable.add_account_mini;
			}
		}
	}
}
