package com.elfec.ssc.view.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.TextFormater;
import com.elfec.ssc.helpers.utils.AccountFormatter;
import com.elfec.ssc.model.Account;

public class ViewAccountsAdapter extends ArrayAdapter<Account> {
	private List<Account> accounts;
	private int resource;
	private LayoutInflater inflater = null;
	public ViewAccountsAdapter(Context context, int resource,
			final List<Account> accounts) {
		super(context, resource, accounts);
		this.accounts = accounts;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return accounts.size();
	}

	@Override
	public Account getItem(int position) {
		return accounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		Account account = accounts.get(position);
		((TextView) convertView.findViewById(R.id.row_account_value)).setText(
				AccountFormatter.formatAccountNumber(account.getAccountNumber()));
		((TextView) convertView.findViewById(R.id.row_nus_value))
				.setText(account.getNUS());
		((TextView) convertView.findViewById(R.id.row_name_value))
		.setText(TextFormater.capitalize(account.getAccountOwner()));

		((TextView) convertView.findViewById(R.id.total_amount))
		.setText((account.getTotalDebt().toString()+" Bs."));

		return convertView;
	}
	

}
