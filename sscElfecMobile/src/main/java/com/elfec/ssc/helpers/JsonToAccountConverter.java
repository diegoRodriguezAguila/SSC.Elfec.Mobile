package com.elfec.ssc.helpers;

import android.util.Log;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Debt;

import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Clase que sirve para convertir un JSONObject a una cuenta con su informaci�n
 * completa
 * 
 * @author drodriguez
 *
 */
public class JsonToAccountConverter {
	private static final String TAG = "JsonToAccountConverter";

	/**
	 * Convierte un JSONObject a una cuenta con todas sus deudas
	 * 
	 * @param accObj json object of account
	 * @return account
	 */
	public static Account convert(JSONObject accObj) {
		try {
			Log.d(TAG, "Received: "+accObj.toString());
			String latitudeStr = accObj
                    .getString("Latitude");
            String longitudeStr = accObj.getString("Longitude");
			Account account = new Account(Client.getActiveClient(),
					accObj.getString("AccountNumber"), accObj.getString("NUS"),
					accObj.getString("AccountOwner"),
					accObj.getString("Address"),
					isStringNull(latitudeStr)?0:Double.parseDouble(latitudeStr.replace(',', '.')),
					isStringNull(longitudeStr)?0:Double.parseDouble(longitudeStr.replace(',', '.')),
                    Short.parseShort(accObj.getString("EnergySupplyStatus")));
			JSONArray debtArr = accObj.getJSONArray("Debts");
			int lenght = debtArr.length();
			for (int i = 0; i < lenght; i++) {
				JSONObject debtObj = debtArr.getJSONObject(i);
				if (!debtObj.getString("Amount").equals("0")) {
					account.getDebts()
							.add(new Debt(
									account,
									new BigDecimal(debtObj.getString("Amount")
											.replace(',', '.')),
									debtObj.getInt("Year"),
									Short.parseShort(debtObj.getString("Month")),
									debtObj.getInt("ReceiptNumber"),
									DateTimeFormat
											.forPattern("dd/MM/yy")
											.parseDateTime(
													debtObj.getString("ExpirationDate"))));
				}
			}
			return account;
		} catch (JSONException e) {
            e.printStackTrace();
		}
		return null;
	}

    private static boolean isStringNull(String string){
        return string==null || string.equals("null")
                || string.equals("\"null\"") || string.equals("'null'");
    }

}
