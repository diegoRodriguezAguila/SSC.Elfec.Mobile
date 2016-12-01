package com.elfec.ssc.helpers;

import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.Account;
import com.google.gson.Gson;

/**
 * Clase que sirve para convertir un JSONObject a una cuenta con su información
 * completa
 *
 * @author drodriguez
 */
public class JsonToAccountConverter {

    /**
     * Convierte un JSONObject a una cuenta con todas sus deudas
     *
     * @param json json representation of account
     * @return account
     */
    public static Account convert(String json) {
        if (json == null) return null;
        Gson gson = GsonUtils.generateGson();
        return gson.fromJson(json, Account.class);
    }
}
