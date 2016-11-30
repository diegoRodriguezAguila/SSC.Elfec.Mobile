package com.elfec.ssc.helpers;

import android.util.Log;

import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Debt;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase que sirve para convertir un JSONObject a una cuenta con su informaci�n
 * completa
 *
 * @author drodriguez
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
            Log.d(TAG, "Received: " + accObj.toString());
            String latitudeStr = accObj
                    .getString("Latitude");
            String longitudeStr = accObj.getString("Longitude");
            Account account = new Account(Client.getActiveClient(),
                    accObj.getString("AccountNumber"), accObj.getString("NUS"),
                    accObj.getString("AccountOwner"),
                    accObj.getString("Address"),
                    isStringNull(latitudeStr) ? 0 : Double.parseDouble(latitudeStr.replace(',', '.')),
                    isStringNull(longitudeStr) ? 0 : Double.parseDouble(longitudeStr.replace(',', '.')),
                    Short.parseShort(accObj.getString("EnergySupplyStatus")));
            parseDebts(accObj, account);
            return account;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseDebts(JSONObject accObj, Account account) throws JSONException {
        String strDebts = accObj.optString("Debts", null);
        Gson gson = GsonUtils.generateGson();
        Debt[] debts = gson.fromJson(strDebts, Debt[].class);
        List<Debt> debtList = account.getDebts();
        for (Debt debt : debts) {
            if (debt.getAmount().equals(BigDecimal.ZERO))
                continue;
            debtList.add(debt);
        }
    }

    private static boolean isStringNull(String string) {
        return string == null || string.equals("null")
                || string.equals("\"null\"") || string.equals("'null'");
    }

}
