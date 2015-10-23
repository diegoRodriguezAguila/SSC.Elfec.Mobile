package com.elfec.ssc.helpers;

import java.math.BigDecimal;

import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Debt;

/**
 * Clase que sirve para convertir un JSONObject a una cuenta con su información
 * completa
 * 
 * @author drodriguez
 *
 */
public class JsonToAccountConverter {

	/**
	 * Convierte un JSONObject a una cuenta con todas sus deudas
	 * 
	 * @param accObj
	 * @return
	 */
	public static Account convert(JSONObject accObj) {
		try {
			Account account = new Account(Client.getActiveClient(),
					accObj.getString("AccountNumber"), accObj.getString("NUS"),
					accObj.getString("AccountOwner"),
					accObj.getString("Address"), Double.parseDouble(accObj
							.getString("Latitude").replace(',', '.')),
					Double.parseDouble(accObj.getString("Longitude").replace(
							',', '.')), Short.parseShort(accObj
							.getString("EnergySupplyStatus")));
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
		}
		return null;
	}

}
