package com.elfec.ssc.helpers;

import android.util.Log;

import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Debt;
import com.google.gson.Gson;

import org.json.JSONException;

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
     * @param json json representation of account
     * @return account
     */
    public static Account convert(String json) {
        try {
            Log.d(TAG, "Received: " + json);
            Gson gson = GsonUtils.generateGson();
            Account account = gson.fromJson(json, Account.class);
            account.setClient(Client.getActiveClient());
            filterDebts(account);
            return account;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void filterDebts(Account account) throws JSONException {
        List<Debt> debts = account.getDebts();
        for (int i = 0; i < debts.size(); i++) {
            if (BigDecimal.ZERO.equals(debts.get(i).getAmount())) {
                debts.remove(i);
                i--;
            }
        }
    }

}
