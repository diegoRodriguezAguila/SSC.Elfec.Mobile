package com.elfec.ssc.view.adapters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.TextFormater;
import com.elfec.ssc.helpers.utils.AccountFormatter;
import com.elfec.ssc.model.Account;

public class ViewAccountsAdapter extends ArrayAdapter<Account> {
	private List<Account> accounts;
	private int resource;
	private LayoutInflater inflater = null;
	private NumberFormat nf;
	public ViewAccountsAdapter(Context context, int resource,
			final List<Account> accounts) {
		super(context, resource, accounts);
		this.accounts = accounts;
		this.resource = resource;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
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
		setAccountBalanceInformation(convertView, account);

		return convertView;
	}

	private void setAccountBalanceInformation(View convertView, Account account) {
		TextView txtTotalAmount = ((TextView) convertView.findViewById(R.id.total_amount));
		BigDecimal totalDebt = account.getTotalDebt();
		if (totalDebt.compareTo(BigDecimal.ZERO) == 0)
		{
			((LinearLayout)convertView.findViewById(R.id.layout_decimal_debt))
				.setVisibility(View.GONE);
			txtTotalAmount.setTextSize(18);
			txtTotalAmount.setText(R.string.lbl_no_debts);
		}
		else
		{
			((LinearLayout)convertView.findViewById(R.id.layout_decimal_debt))
			.setVisibility(View.VISIBLE);
			txtTotalAmount.setText(nf.format
					(account.getTotalDebt().toBigInteger().doubleValue()));
			
			((TextView) convertView.findViewById(R.id.total_amount_decimal))
			.setText((account.getTotalDebt().remainder(BigDecimal.ONE).multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.CEILING).toString()));
		}
	}
	

}
