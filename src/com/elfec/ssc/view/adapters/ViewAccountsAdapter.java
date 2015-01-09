package com.elfec.ssc.view.adapters;

import java.util.List;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ViewAccountsAdapter extends ArrayAdapter<Account> {
	private List<Account> accounts;
	private static LayoutInflater inflater = null;
	public ViewAccountsAdapter(Context context, int resource,
			final List<Account> accounts) {
		super(context, resource, accounts);
		try {
			this.accounts = accounts;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		} catch (Exception e) {

		}
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
		View vi = convertView;
		vi = inflater.inflate(R.layout.view_accounts_row, null);
		Account dispositivoBluetooth = accounts.get(position);
		((TextView) vi.findViewById(R.id.rowTextView1)).setText(""
				+ dispositivoBluetooth.getAccountNumber());
		((TextView) vi.findViewById(R.id.rowTextView2))
				.setText(dispositivoBluetooth.getNUS());
		return vi;
	}
	

}
