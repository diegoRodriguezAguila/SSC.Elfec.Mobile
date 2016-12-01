package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.webservices.IWSResultConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GetAllAccountsWSConverter implements IWSResultConverter<List<Account>> {

    @Override
    public List<Account> convert(String result) {
        if (result == null) return new ArrayList<>();
        Gson gson = GsonUtils.generateGson();
        return Arrays.asList(gson.fromJson(result, Account[].class));
    }

}
