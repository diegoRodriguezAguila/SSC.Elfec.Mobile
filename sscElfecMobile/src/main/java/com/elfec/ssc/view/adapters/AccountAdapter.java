package com.elfec.ssc.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.elfec.ssc.model.Account;
import com.elfec.ssc.view.adapters.viewholders.AccountViewHolder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

public class AccountAdapter extends ArrayAdapter<Account> {
	private int mResourceId;
	private LayoutInflater mInflater;
	private NumberFormat mNumFormat;

	public AccountAdapter(Context context, int resource,
						  final List<Account> accounts) {
		super(context, resource, accounts);
		this.mResourceId = resource;
		mInflater = LayoutInflater.from(context);
		mNumFormat = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat) mNumFormat).setDecimalFormatSymbols(customSymbol);
		mNumFormat.setGroupingUsed(true);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AccountViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(mResourceId, parent, false);
			viewHolder = new AccountViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else
			viewHolder = (AccountViewHolder) convertView.getTag();
		viewHolder.bindAccount(getItem(position), mNumFormat);
		return convertView;
	}
}
