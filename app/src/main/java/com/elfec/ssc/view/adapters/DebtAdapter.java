package com.elfec.ssc.view.adapters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.utils.TextFormatter;
import com.elfec.ssc.model.Debt;

public class DebtAdapter extends ArrayAdapter<Debt> {

	private List<Debt> debts;
	private int resource;
	private LayoutInflater inflater = null;
	private NumberFormat nf;

	public DebtAdapter(Context context, int resource, final List<Debt> debts) {
		super(context, resource, debts);
		Collections.sort(debts, new Comparator<Debt>() {
			public int compare(Debt debt1, Debt debt2) {
				return (new DateTime(debt2.getYear(), debt2.getMonth(), 1, 0, 0)
						.compareTo(new DateTime(debt1.getYear(), debt1
								.getMonth(), 1, 0, 0)));
			}
		});
		this.debts = debts;
		this.resource = resource;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initializeDecimalFormatter();
	}

	private void initializeDecimalFormatter() {
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat) nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
	}

	@Override
	public int getCount() {
		return debts.size();
	}

	@Override
	public Debt getItem(int position) {
		return debts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(resource, null);
		Debt debt = getItem(position);
		DateTime date = new DateTime(debt.getYear(), debt.getMonth(), 1, 0, 0);
		((TextView) convertView.findViewById(R.id.list_item_moth_year_debt))
				.setText(TextFormatter.capitalize(date.toString("MMMM yyyy")));
		((TextView) convertView.findViewById(R.id.debt_amount)).setText(nf
				.format(debt.getAmount().toBigInteger().doubleValue()));

		((TextView) convertView
				.findViewById(R.id.list_item_expiration_date_debt))
				.setText(debt.getExpirationDate().toString("dd/MM/yyyy"));
		((TextView) convertView
				.findViewById(R.id.list_item_receipt_number_debt)).setText(""
				+ debt.getReceiptNumber());
		String decimalPart = (debt.getAmount().remainder(BigDecimal.ONE)
				.multiply(new BigDecimal("100"))
				.setScale(0, RoundingMode.CEILING).toString());
		if (decimalPart.equals("0"))
			decimalPart += "0";
		((TextView) convertView.findViewById(R.id.debt_amount_decimal))
				.setText(decimalPart);
		return convertView;
	}

}
