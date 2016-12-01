package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.helpers.JsonToAccountConverter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.webservices.ResultConverter;

public class RegisterAccountWSConverter implements ResultConverter<Account> {

    @Override
    public Account convert(String result) {
        if (result == null) return null;
        return JsonToAccountConverter.convert(result);
    }
}
