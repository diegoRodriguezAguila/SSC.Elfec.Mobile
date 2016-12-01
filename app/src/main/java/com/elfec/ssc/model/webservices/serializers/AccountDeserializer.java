package com.elfec.ssc.model.webservices.serializers;

import android.util.Log;

import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Debt;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

/**
 * GSON serialiser/deserialiser for converting {@link Account} objects.
 */
public class AccountDeserializer implements JsonDeserializer<Account> {

    private static final String TAG = "AccountDeserializer";

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type. <p>
     * <p>
     * In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeOfT}
     */
    @Override
    public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // Do not try to deserialize null or empty values
        if (json.isJsonNull()) {
            return null;
        }
        try {
            return convert(json, typeOfT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convierte un JSONObject a una cuenta con todas sus deudas
     *
     * @param json json representation of account
     * @return account
     */
    private Account convert(JsonElement json, Type typeOfT) {
        Log.d(TAG, "Received: " + json.toString());
        Account account = GsonUtils.generateBaseGson().fromJson(json, typeOfT);
        account.setClient(Client.getActiveClient());
        filterDebts(account);
        return account;
    }

    private void filterDebts(Account account) {
        List<Debt> debts = account.getDebts();
        for (int i = 0; i < debts.size(); i++) {
            if (BigDecimal.ZERO.equals(debts.get(i).getAmount())) {
                debts.remove(i);
                i--;
            }
        }
    }
}