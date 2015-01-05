package com.elfec.ssc.model.webservices;

import java.util.ArrayList;
import java.util.List;

import com.elfec.ssc.model.Account;


public class GetAllAccountsWSConverter implements IWSResultConverter<List<Account>>  {

	@Override
	public List<Account> convert(String result) {
		List<Account> accounts=new ArrayList<Account>();
		Account account=new Account();
		account.setAccountNumber("26");
		account.setClientId(1);
		account.setNUS(result);
		accounts.add(account);
		return accounts;
	}

}
