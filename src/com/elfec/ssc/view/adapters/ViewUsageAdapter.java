package com.elfec.ssc.view.adapters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Usage;

public class ViewUsageAdapter extends ArrayAdapter<Usage> {
	private List<Usage> usage;
	private int resource;
	private LayoutInflater inflater = null;
	private NumberFormat nf;

	public ViewUsageAdapter(Context context, int resource,
			final List<Usage> usage) {
		super(context, resource, usage);
		this.usage = usage;
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
		return usage.size();
	}

	@Override
	public Usage getItem(int position) {
		return usage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView = inflater.inflate(resource, null);
		Usage u = usage.get(position);
		((TextView) convertView.findViewById(R.id.term_text)).setText(u.getTerm());
		((ProgressBar) convertView.findViewById(R.id.progressBar1)).setProgress(100);

		((TextView) convertView.findViewById(R.id.usage_text))
		.setText(u.getEnergyUsage());
	
		return convertView;
	}

	
	
}
